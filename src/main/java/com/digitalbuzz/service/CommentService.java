package com.digitalbuzz.service;

import com.digitalbuzz.dto.request.CommentRequest;
import com.digitalbuzz.dto.response.CommentResponse;
import com.digitalbuzz.dto.response.PageResponse;
import com.digitalbuzz.exception.ResourceNotFoundException;
import com.digitalbuzz.exception.UnauthorizedException;
import com.digitalbuzz.model.Comment;
import com.digitalbuzz.model.NewsArticle;
import com.digitalbuzz.model.User;
import com.digitalbuzz.model.enums.CommentStatus;
import com.digitalbuzz.model.enums.Role;
import com.digitalbuzz.repository.ArticleRepository;
import com.digitalbuzz.repository.CommentRepository;
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

@Service
public class CommentService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;
    
    public CommentService(CommentRepository commentRepository,
                         ArticleRepository articleRepository,
                         UserRepository userRepository,
                         EntityMapper entityMapper) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
    }
    
    @Transactional
    public CommentResponse addComment(String username, Long articleId, CommentRequest request) {
        logger.info("Adding comment to article ID: {} by user: {}", articleId, username);
        
        NewsArticle article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", articleId));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        Comment comment = Comment.builder()
                .article(article)
                .user(user)
                .content(request.getContent())
                .status(CommentStatus.PENDING)
                .build();
        
        comment = commentRepository.save(comment);
        logger.info("Comment added: {}", comment.getId());
        
        return entityMapper.toCommentResponse(comment);
    }
    
    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> getCommentsByArticle(Long articleId, int page, int size) {
        logger.debug("Getting comments for article ID: {}", articleId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> commentPage = commentRepository.findByArticleIdAndStatus(
                articleId, CommentStatus.APPROVED, pageable);
        
        return entityMapper.toPageResponse(commentPage, CommentResponse.class);
    }
    
    @Transactional
    public CommentResponse updateComment(String username, Long commentId, CommentRequest request) {
        logger.info("Updating comment ID: {} by user: {}", commentId, username);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only update your own comments");
        }
        
        comment.setContent(request.getContent());
        comment = commentRepository.save(comment);
        logger.info("Comment updated: {}", commentId);
        
        return entityMapper.toCommentResponse(comment);
    }
    
    @Transactional
    public void deleteComment(String username, Long commentId) {
        logger.info("Deleting comment ID: {} by user: {}", commentId, username);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        if (!comment.getUser().getId().equals(user.getId()) && 
                user.getRole() != Role.ADMIN && user.getRole() != Role.EDITOR) {
            throw new UnauthorizedException("You don't have permission to delete this comment");
        }
        
        commentRepository.delete(comment);
        logger.info("Comment deleted: {}", commentId);
    }
    
    @Transactional
    public CommentResponse approveComment(String username, Long commentId) {
        logger.info("Approving comment ID: {} by user: {}", commentId, username);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.EDITOR) {
            throw new UnauthorizedException("Only editors and admins can approve comments");
        }
        
        comment.setStatus(CommentStatus.APPROVED);
        comment = commentRepository.save(comment);
        logger.info("Comment approved: {}", commentId);
        
        return entityMapper.toCommentResponse(comment);
    }
}
