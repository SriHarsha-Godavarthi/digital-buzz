package com.digitalbuzz.dto.response;

import com.digitalbuzz.model.enums.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long articleId;
    private UserResponse user;
    private String content;
    private CommentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
