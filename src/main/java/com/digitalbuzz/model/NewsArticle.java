package com.digitalbuzz.model;

import com.digitalbuzz.model.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "news_articles",
       indexes = {
           @Index(name = "idx_title", columnList = "title"),
           @Index(name = "idx_status", columnList = "status"),
           @Index(name = "idx_published_at", columnList = "publishedAt"),
           @Index(name = "idx_author", columnList = "author_id")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticle extends BaseEntity {
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(length = 500)
    private String summary;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToMany
    @JoinTable(
        name = "article_tags",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ArticleStatus status = ArticleStatus.DRAFT;
    
    private LocalDateTime publishedAt;
    
    @Column(nullable = false)
    @Builder.Default
    private Long viewCount = 0L;
    
    @Column(length = 255)
    private String featuredImage;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;
}
