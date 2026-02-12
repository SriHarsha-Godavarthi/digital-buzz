package com.digitalbuzz.service;

import com.digitalbuzz.dto.request.TagRequest;
import com.digitalbuzz.dto.response.TagResponse;
import com.digitalbuzz.exception.DuplicateResourceException;
import com.digitalbuzz.exception.ResourceNotFoundException;
import com.digitalbuzz.model.Tag;
import com.digitalbuzz.repository.TagRepository;
import com.digitalbuzz.util.EntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    
    private static final Logger logger = LoggerFactory.getLogger(TagService.class);
    
    private final TagRepository tagRepository;
    private final EntityMapper entityMapper;
    
    public TagService(TagRepository tagRepository, EntityMapper entityMapper) {
        this.tagRepository = tagRepository;
        this.entityMapper = entityMapper;
    }
    
    @Transactional
    public TagResponse createTag(TagRequest request) {
        logger.info("Creating tag: {}", request.getName());
        
        String slug = generateSlug(request.getName());
        
        if (tagRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("Tag", "slug", slug);
        }
        
        Tag tag = Tag.builder()
                .name(request.getName())
                .slug(slug)
                .build();
        
        tag = tagRepository.save(tag);
        logger.info("Tag created: {}", tag.getName());
        
        return entityMapper.toTagResponse(tag);
    }
    
    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        logger.debug("Getting all tags");
        return tagRepository.findAll().stream()
                .map(entityMapper::toTagResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TagResponse getTagById(Long id) {
        logger.debug("Getting tag by ID: {}", id);
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
        return entityMapper.toTagResponse(tag);
    }
    
    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        logger.info("Updating tag ID: {}", id);
        
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
        
        tag.setName(request.getName());
        tag.setSlug(generateSlug(request.getName()));
        
        tag = tagRepository.save(tag);
        logger.info("Tag updated: {}", tag.getName());
        
        return entityMapper.toTagResponse(tag);
    }
    
    @Transactional
    public void deleteTag(Long id) {
        logger.info("Deleting tag ID: {}", id);
        
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag", "id", id);
        }
        
        tagRepository.deleteById(id);
        logger.info("Tag deleted: {}", id);
    }
    
    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}
