package com.digitalbuzz.util;

import com.digitalbuzz.dto.response.*;
import com.digitalbuzz.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    
    UserResponse toUserResponse(User user);
    
    CategoryResponse toCategoryResponse(Category category);
    
    TagResponse toTagResponse(Tag tag);
    
    Set<TagResponse> toTagResponseSet(Set<Tag> tags);
    
    @Mapping(target = "articleId", source = "article.id")
    CommentResponse toCommentResponse(Comment comment);
    
    ArticleResponse toArticleResponse(NewsArticle article);
    
    default <T> PageResponse<T> toPageResponse(Page<?> page, Class<T> responseClass) {
        return PageResponse.<T>builder()
                .content(page.getContent().stream()
                        .map(item -> mapToResponse(item, responseClass))
                        .collect(Collectors.toList()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
    
    @SuppressWarnings("unchecked")
    default <T> T mapToResponse(Object entity, Class<T> responseClass) {
        if (entity instanceof User && responseClass == UserResponse.class) {
            return (T) toUserResponse((User) entity);
        } else if (entity instanceof Category && responseClass == CategoryResponse.class) {
            return (T) toCategoryResponse((Category) entity);
        } else if (entity instanceof Tag && responseClass == TagResponse.class) {
            return (T) toTagResponse((Tag) entity);
        } else if (entity instanceof NewsArticle && responseClass == ArticleResponse.class) {
            return (T) toArticleResponse((NewsArticle) entity);
        } else if (entity instanceof Comment && responseClass == CommentResponse.class) {
            return (T) toCommentResponse((Comment) entity);
        }
        throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass());
    }
}
