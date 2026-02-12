package com.digitalbuzz.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "slug")
       },
       indexes = {
           @Index(name = "idx_slug", columnList = "slug")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false, unique = true, length = 100)
    private String slug;
}
