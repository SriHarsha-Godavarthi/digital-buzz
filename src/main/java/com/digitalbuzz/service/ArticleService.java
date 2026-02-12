package com.digitalbuzz.service;

import com.digitalbuzz.dto.request.ArticleRequest;
import com.digitalbuzz.dto.response.ArticleResponse;
import com.digitalbuzz.dto.response.PageResponse;
import com.digitalbuzz.exception.ResourceNotFoundException;
import com.digitalbuzz.exception.UnauthorizedException;
import com.digitalbuzz.model.Category;
import com.digitalbuzz.model.NewsArticle;
import com.digitalbuzz.model.Tag;
import com.digitalbuzz.model.User;
import com.digitalbuzz.model.enums.ArticleStatus;
import com.digitalbuzz.model.enums.Role;
import com.digitalbuzz.repository.ArticleRepository;
import com.digitalbuzz.repository.CategoryRepository;
import com.digitalbuzz.repository.TagRepository;
import com.digitalbuzz.repository.UserRepository;
import com.digitalbuzz.util.EntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class ArticleService {
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);
    
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final EntityMapper entityMapper;
    
    public ArticleService(ArticleRepository articleRepository,
                         UserRepository userRepository,
                         CategoryRepository categoryRepository,
                         TagRepository tagRepository,
                         EntityMapper entityMapper) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.entityMapper = entityMapper;
    }
    
    @Transactional
    public ArticleResponse createArticle(String username, ArticleRequest request) {
        logger.info("Creating article: {} by user: {}", request.getTitle(), username);
        
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        
        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = tagRepository.findByIdIn(request.getTagIds());
        }
        
        NewsArticle article = NewsArticle.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .summary(request.getSummary())
                .author(author)
                .category(category)
                .tags(tags)
                .status(ArticleStatus.DRAFT)
                .featuredImage(request.getFeaturedImage())
                .viewCount(0L)
                .deleted(false)
                .build();
        
        article = articleRepository.save(article);
        logger.info("Article created: {}", article.getTitle());
        
        return entityMapper.toArticleResponse(article);
    }
    
    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getAllArticles(int page, int size, String sortBy, String direction) {
        logger.debug("Getting all articles - page: {}, size: {}", page, size);
        
        Sort sort = direction.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NewsArticle> articlePage = articleRepository.findByDeletedFalse(pageable);
        
        return entityMapper.toPageResponse(articlePage, ArticleResponse.class);
    }
    
    @Transactional
    public ArticleResponse getArticleById(Long id) {
        logger.debug("Getting article by ID: {}", id);
        
        NewsArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
        
        if (article.getDeleted()) {
            throw new ResourceNotFoundException("Article", "id", id);
        }
        
        article.setViewCount(article.getViewCount() + 1);
        article = articleRepository.save(article);
        
        return entityMapper.toArticleResponse(article);
    }
    
    @Transactional
    public ArticleResponse updateArticle(String username, Long id, ArticleRequest request) {
        logger.info("Updating article ID: {} by user: {}", id, username);
        
        NewsArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
        
        if (article.getDeleted()) {
            throw new ResourceNotFoundException("Article", "id", id);
        }
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        if (!article.getAuthor().getId().equals(user.getId()) && 
                user.getRole() != Role.ADMIN && user.getRole() != Role.EDITOR) {
            throw new UnauthorizedException("You don't have permission to update this article");
        }
        
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        
        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = tagRepository.findByIdIn(request.getTagIds());
        }
        
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSummary(request.getSummary());
        article.setCategory(category);
        article.setTags(tags);
        article.setFeaturedImage(request.getFeaturedImage());
        
        article = articleRepository.save(article);
        logger.info("Article updated: {}", article.getTitle());
        
        return entityMapper.toArticleResponse(article);
    }
    
    @Transactional
    public void deleteArticle(String username, Long id) {
        logger.info("Deleting article ID: {} by user: {}", id, username);
        
        NewsArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        if (!article.getAuthor().getId().equals(user.getId()) && 
                user.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("You don't have permission to delete this article");
        }
        
        article.setDeleted(true);
        articleRepository.save(article);
        logger.info("Article soft deleted: {}", id);
    }
    
    @Transactional
    public ArticleResponse publishArticle(String username, Long id) {
        logger.info("Publishing article ID: {} by user: {}", id, username);
        
        NewsArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.EDITOR) {
            throw new UnauthorizedException("Only editors and admins can publish articles");
        }
        
        article.setStatus(ArticleStatus.PUBLISHED);
        article.setPublishedAt(LocalDateTime.now());
        
        article = articleRepository.save(article);
        logger.info("Article published: {}", article.getTitle());
        
        return entityMapper.toArticleResponse(article);
    }
    
    @Transactional
    public ArticleResponse archiveArticle(String username, Long id) {
        logger.info("Archiving article ID: {} by user: {}", id, username);
        
        NewsArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        if (!article.getAuthor().getId().equals(user.getId()) && 
                user.getRole() != Role.ADMIN && user.getRole() != Role.EDITOR) {
            throw new UnauthorizedException("You don't have permission to archive this article");
        }
        
        article.setStatus(ArticleStatus.ARCHIVED);
        article = articleRepository.save(article);
        logger.info("Article archived: {}", article.getTitle());
        
        return entityMapper.toArticleResponse(article);
    }
    
    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> searchArticles(String keyword, int page, int size) {
        logger.debug("Searching articles with keyword: {}", keyword);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<NewsArticle> articlePage = articleRepository.searchArticles(keyword, pageable);
        
        return entityMapper.toPageResponse(articlePage, ArticleResponse.class);
    }
    
    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> getArticlesByCategory(Long categoryId, int page, int size) {
        logger.debug("Getting articles by category ID: {}", categoryId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<NewsArticle> articlePage = articleRepository.findByCategoryIdAndDeletedFalse(categoryId, pageable);
        
        return entityMapper.toPageResponse(articlePage, ArticleResponse.class);
    }
}
