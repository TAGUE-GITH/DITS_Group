package com.dits.dits_group.article.mapper;

import com.dits.dits_group.article.dto.ArticleRequest;
import com.dits.dits_group.article.dto.ArticleResponse;
import com.dits.dits_group.article.entity.Article;

import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    // ==========================================
    // ENTITY -> RESPONSE
    // ==========================================

    public ArticleResponse toResponse(
            Article article
    ) {

        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .summary(article.getSummary())
                .content(article.getContent())
                .imageUrl(article.getImageUrl())
                .author(article.getAuthor())
                .publicationDate(
                        article.getPublicationDate()
                )
                .published(
                        article.isPublished()
                )
                .createdAt(
                        article.getCreatedAt()
                )
                .updatedAt(
                        article.getUpdatedAt()
                )
                .build();
    }

    // ==========================================
    // REQUEST -> ENTITY
    // ==========================================

    public Article toEntity(
            ArticleRequest request
    ) {

        return Article.builder()
                .title(request.getTitle())
                .summary(request.getSummary())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .author(request.getAuthor())
                .publicationDate(
                        request.getPublicationDate()
                )
                .published(
                        request.getPublished() != null
                                && request.getPublished()
                )
                .build();
    }

    // ==========================================
    // UPDATE ENTITY
    // ==========================================

    public void updateEntity(
            Article article,
            ArticleRequest request
    ) {

        article.setTitle(
                request.getTitle()
        );

        article.setSummary(
                request.getSummary()
        );

        article.setContent(
                request.getContent()
        );

        article.setImageUrl(
                request.getImageUrl()
        );

        article.setAuthor(
                request.getAuthor()
        );

        article.setPublicationDate(
                request.getPublicationDate()
        );

        /*
         * Si published n'est pas envoyé,
         * on conserve le statut actuel.
         */
        if (request.getPublished() != null) {
            article.setPublished(
                    request.getPublished()
            );
        }
    }
}