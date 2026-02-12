package com.digitalbuzz.repository;

import com.digitalbuzz.model.NewsArticle;
import com.digitalbuzz.model.enums.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<NewsArticle, Long> {
    
    Page<NewsArticle> findByDeletedFalse(Pageable pageable);
    
    Page<NewsArticle> findByStatusAndDeletedFalse(ArticleStatus status, Pageable pageable);
    
    Page<NewsArticle> findByCategoryIdAndDeletedFalse(Long categoryId, Pageable pageable);
    
    Page<NewsArticle> findByAuthorIdAndDeletedFalse(Long authorId, Pageable pageable);
    
    @Query("SELECT a FROM NewsArticle a WHERE a.deleted = false AND " +
           "(LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.summary) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<NewsArticle> searchArticles(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT a FROM NewsArticle a JOIN a.tags t WHERE t.id = :tagId AND a.deleted = false")
    Page<NewsArticle> findByTagId(@Param("tagId") Long tagId, Pageable pageable);
}
