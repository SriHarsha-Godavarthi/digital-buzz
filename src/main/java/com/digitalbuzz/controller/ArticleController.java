package com.digitalbuzz.controller;

import com.digitalbuzz.dto.request.ArticleRequest;
import com.digitalbuzz.dto.response.ArticleResponse;
import com.digitalbuzz.dto.response.PageResponse;
import com.digitalbuzz.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@Tag(name = "Articles", description = "News article management endpoints")
public class ArticleController {
    
    private final ArticleService articleService;
    
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('AUTHOR', 'EDITOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create article", description = "Create a new article (Author, Editor, Admin)")
    public ResponseEntity<ArticleResponse> createArticle(
            Authentication authentication,
            @Valid @RequestBody ArticleRequest request) {
        ArticleResponse response = articleService.createArticle(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all articles", description = "Get all articles with pagination and sorting")
    public ResponseEntity<PageResponse<ArticleResponse>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        PageResponse<ArticleResponse> response = articleService.getAllArticles(page, size, sortBy, direction);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get article by ID", description = "Get a specific article by its ID")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id) {
        ArticleResponse response = articleService.getArticleById(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AUTHOR', 'EDITOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update article", description = "Update an article (owner, Editor, Admin)")
    public ResponseEntity<ArticleResponse> updateArticle(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ArticleRequest request) {
        ArticleResponse response = articleService.updateArticle(authentication.getName(), id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete article", description = "Soft delete an article (owner, Admin)")
    public ResponseEntity<Void> deleteArticle(
            Authentication authentication,
            @PathVariable Long id) {
        articleService.deleteArticle(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Publish article", description = "Publish an article (Editor, Admin)")
    public ResponseEntity<ArticleResponse> publishArticle(
            Authentication authentication,
            @PathVariable Long id) {
        ArticleResponse response = articleService.publishArticle(authentication.getName(), id);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('AUTHOR', 'EDITOR', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Archive article", description = "Archive an article (owner, Editor, Admin)")
    public ResponseEntity<ArticleResponse> archiveArticle(
            Authentication authentication,
            @PathVariable Long id) {
        ArticleResponse response = articleService.archiveArticle(authentication.getName(), id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search articles", description = "Search articles by title, content, or summary")
    public ResponseEntity<PageResponse<ArticleResponse>> searchArticles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<ArticleResponse> response = articleService.searchArticles(keyword, page, size);
        return ResponseEntity.ok(response);
    }
}
