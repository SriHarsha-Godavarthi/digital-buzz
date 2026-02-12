package com.digitalbuzz.controller;

import com.digitalbuzz.dto.request.CommentRequest;
import com.digitalbuzz.dto.response.CommentResponse;
import com.digitalbuzz.dto.response.PageResponse;
import com.digitalbuzz.service.CommentService;
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
@RequestMapping
@Tag(name = "Comments", description = "Comment management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {
    
    private final CommentService commentService;
    
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    @PostMapping("/articles/{id}/comments")
    @Operation(summary = "Add comment", description = "Add a comment to an article")
    public ResponseEntity<CommentResponse> addComment(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse response = commentService.addComment(authentication.getName(), id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/articles/{id}/comments")
    @Operation(summary = "Get comments", description = "Get all approved comments for an article")
    public ResponseEntity<PageResponse<CommentResponse>> getCommentsByArticle(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<CommentResponse> response = commentService.getCommentsByArticle(id, page, size);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/comments/{id}")
    @Operation(summary = "Update comment", description = "Update a comment (owner only)")
    public ResponseEntity<CommentResponse> updateComment(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse response = commentService.updateComment(authentication.getName(), id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/comments/{id}")
    @Operation(summary = "Delete comment", description = "Delete a comment (owner, Editor, Admin)")
    public ResponseEntity<Void> deleteComment(
            Authentication authentication,
            @PathVariable Long id) {
        commentService.deleteComment(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/comments/{id}/approve")
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @Operation(summary = "Approve comment", description = "Approve a comment (Editor, Admin)")
    public ResponseEntity<CommentResponse> approveComment(
            Authentication authentication,
            @PathVariable Long id) {
        CommentResponse response = commentService.approveComment(authentication.getName(), id);
        return ResponseEntity.ok(response);
    }
}
