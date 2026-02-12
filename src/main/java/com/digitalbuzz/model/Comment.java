package com.digitalbuzz.model;

import com.digitalbuzz.model.enums.CommentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments",
       indexes = {
           @Index(name = "idx_article", columnList = "article_id"),
           @Index(name = "idx_user", columnList = "user_id"),
           @Index(name = "idx_status", columnList = "status")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private NewsArticle article;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CommentStatus status = CommentStatus.PENDING;
}
