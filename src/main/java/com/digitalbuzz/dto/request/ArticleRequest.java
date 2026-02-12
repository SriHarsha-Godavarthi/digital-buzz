package com.digitalbuzz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @Size(max = 500, message = "Summary must not exceed 500 characters")
    private String summary;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    private Set<Long> tagIds;
    
    @Size(max = 255, message = "Featured image URL must not exceed 255 characters")
    private String featuredImage;
}
