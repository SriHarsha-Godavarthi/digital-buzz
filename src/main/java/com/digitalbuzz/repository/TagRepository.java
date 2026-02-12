package com.digitalbuzz.repository;

import com.digitalbuzz.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findBySlug(String slug);
    boolean existsBySlug(String slug);
    Set<Tag> findByIdIn(Set<Long> ids);
}
