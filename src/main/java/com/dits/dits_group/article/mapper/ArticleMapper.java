package com.dits.dits_group.article.mapper;

import com.dits.dits_group.article.dto.ArticleRequest;
import com.dits.dits_group.article.dto.ArticleResponse;
import com.dits.dits_group.article.entity.Article;

import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .summary(article.getSummary())
                .content(article.getContent())
                .imageUrl(article.getImageUrl())
                .author(article.getAuthor())
                .publicationDate(article.getPublicationDate())
                .published(article.isPublished())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    public Article toEntity(ArticleRequest request) {
        return Article.builder()
                .title(request.getTitle())
                .summary(request.getSummary())
                .content(request.getContent())
                .author(request.getAuthor())
                .publicationDate(request.getPublicationDate())
                .published(Boolean.TRUE.equals(request.getPublished()))
                .build();
    }

    public void updateEntity(Article article, ArticleRequest request) {
        article.setTitle(request.getTitle());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setAuthor(request.getAuthor());
        article.setPublicationDate(request.getPublicationDate());

        if (request.getPublished() != null) {
            article.setPublished(request.getPublished());
        }
    }
}