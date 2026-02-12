package com.digitalbuzz.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tags",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "slug")
       },
       indexes = {
           @Index(name = "idx_tag_slug", columnList = "slug")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends BaseEntity {
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true, length = 100)
    private String slug;
}
