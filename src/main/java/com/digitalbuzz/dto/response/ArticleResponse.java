package com.digitalbuzz.dto.response;

import com.digitalbuzz.model.enums.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private UserResponse author;
    private CategoryResponse category;
    private Set<TagResponse> tags;
    private ArticleStatus status;
    private LocalDateTime publishedAt;
    private Long viewCount;
    private String featuredImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
