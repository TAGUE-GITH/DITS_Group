package com.dits.dits_group.article.service;

import com.dits.dits_group.article.dto.ArticleRequest;
import com.dits.dits_group.article.dto.ArticleResponse;
import com.dits.dits_group.article.entity.Article;
import com.dits.dits_group.article.mapper.ArticleMapper;
import com.dits.dits_group.article.repository.ArticleRepository;
import com.dits.dits_group.common.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(
            ArticleRepository articleRepository,
            ArticleMapper articleMapper
    ) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    // ==========================================
    // PUBLIC : ARTICLES PUBLIÉS
    // ==========================================

    public List<ArticleResponse> findAllPublished() {

        return articleRepository
                .findByPublishedTrueOrderByPublicationDateDesc()
                .stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    // ==========================================
    // PUBLIC : DÉTAIL D'UN ARTICLE PUBLIÉ
    // ==========================================

    public ArticleResponse findPublishedById(
            Long id
    ) {

        Article article = articleRepository
                .findByIdAndPublishedTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Article introuvable ou indisponible."
                        )
                );

        return articleMapper.toResponse(article);
    }

    // ==========================================
    // ADMIN : TOUS LES ARTICLES
    // ==========================================

    public List<ArticleResponse> findAll() {

        return articleRepository
                .findAll()
                .stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    // ==========================================
    // ADMIN : DÉTAIL PAR ID
    // ==========================================

    public ArticleResponse findById(
            Long id
    ) {

        Article article =
                getArticleOrThrow(id);

        return articleMapper.toResponse(article);
    }

    // ==========================================
    // ADMIN : CRÉER
    // ==========================================

    @Transactional
    public ArticleResponse create(
            ArticleRequest request
    ) {

        Article article =
                articleMapper.toEntity(request);

        Article savedArticle =
                articleRepository.save(article);

        return articleMapper.toResponse(
                savedArticle
        );
    }

    // ==========================================
    // ADMIN : MODIFIER
    // ==========================================

    @Transactional
    public ArticleResponse update(
            Long id,
            ArticleRequest request
    ) {

        Article article =
                getArticleOrThrow(id);

        articleMapper.updateEntity(
                article,
                request
        );

        Article updatedArticle =
                articleRepository.save(article);

        return articleMapper.toResponse(
                updatedArticle
        );
    }

    // ==========================================
    // ADMIN : PUBLIER / METTRE EN BROUILLON
    // ==========================================

    @Transactional
    public ArticleResponse togglePublished(
            Long id
    ) {

        Article article =
                getArticleOrThrow(id);

        article.setPublished(
                !article.isPublished()
        );

        Article updatedArticle =
                articleRepository.save(article);

        return articleMapper.toResponse(
                updatedArticle
        );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @Transactional
    public void delete(
            Long id
    ) {

        Article article =
                getArticleOrThrow(id);

        articleRepository.delete(article);
    }

    // ==========================================
    // MÉTHODE INTERNE
    // ==========================================

    private Article getArticleOrThrow(
            Long id
    ) {

        return articleRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Article introuvable."
                        )
                );
    }
}