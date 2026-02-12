package com.digitalbuzz.repository;

import com.digitalbuzz.model.Comment;
import com.digitalbuzz.model.enums.CommentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByArticleId(Long articleId, Pageable pageable);
    Page<Comment> findByArticleIdAndStatus(Long articleId, CommentStatus status, Pageable pageable);
    Page<Comment> findByUserId(Long userId, Pageable pageable);
}
