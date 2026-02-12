package com.digitalbuzz.controller;

import com.digitalbuzz.dto.request.TagRequest;
import com.digitalbuzz.dto.response.TagResponse;
import com.digitalbuzz.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@Tag(name = "Tags", description = "Tag management endpoints")
public class TagController {
    
    private final TagService tagService;
    
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create tag", description = "Create a new tag (Admin and Editor only)")
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest request) {
        TagResponse response = tagService.createTag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all tags", description = "Get list of all tags")
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> response = tagService.getAllTags();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get tag by ID", description = "Get a tag by its ID")
    public ResponseEntity<TagResponse> getTagById(@PathVariable Long id) {
        TagResponse response = tagService.getTagById(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update tag", description = "Update a tag (Admin and Editor only)")
    public ResponseEntity<TagResponse> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequest request) {
        TagResponse response = tagService.updateTag(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete tag", description = "Delete a tag (Admin only)")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
