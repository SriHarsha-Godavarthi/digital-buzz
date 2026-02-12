package com.digitalbuzz.service;

import com.digitalbuzz.dto.request.CategoryRequest;
import com.digitalbuzz.dto.response.CategoryResponse;
import com.digitalbuzz.exception.DuplicateResourceException;
import com.digitalbuzz.exception.ResourceNotFoundException;
import com.digitalbuzz.model.Category;
import com.digitalbuzz.repository.CategoryRepository;
import com.digitalbuzz.util.EntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    
    private final CategoryRepository categoryRepository;
    private final EntityMapper entityMapper;
    
    public CategoryService(CategoryRepository categoryRepository, EntityMapper entityMapper) {
        this.categoryRepository = categoryRepository;
        this.entityMapper = entityMapper;
    }
    
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        logger.info("Creating category: {}", request.getName());
        
        String slug = generateSlug(request.getName());
        
        if (categoryRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("Category", "slug", slug);
        }
        
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .slug(slug)
                .build();
        
        category = categoryRepository.save(category);
        logger.info("Category created: {}", category.getName());
        
        return entityMapper.toCategoryResponse(category);
    }
    
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        logger.debug("Getting all categories");
        return categoryRepository.findAll().stream()
                .map(entityMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        logger.debug("Getting category by ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return entityMapper.toCategoryResponse(category);
    }
    
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        logger.info("Updating category ID: {}", id);
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSlug(generateSlug(request.getName()));
        
        category = categoryRepository.save(category);
        logger.info("Category updated: {}", category.getName());
        
        return entityMapper.toCategoryResponse(category);
    }
    
    @Transactional
    public void deleteCategory(Long id) {
        logger.info("Deleting category ID: {}", id);
        
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        
        categoryRepository.deleteById(id);
        logger.info("Category deleted: {}", id);
    }
    
    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}
