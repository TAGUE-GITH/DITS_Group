package com.dits.dits_group.article.service;

import com.dits.dits_group.article.dto.ArticleRequest;
import com.dits.dits_group.article.dto.ArticleResponse;
import com.dits.dits_group.article.entity.Article;
import com.dits.dits_group.article.mapper.ArticleMapper;
import com.dits.dits_group.article.repository.ArticleRepository;
import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.common.storage.FileStorageService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final FileStorageService fileStorageService;

    public ArticleService(
            ArticleRepository articleRepository,
            ArticleMapper articleMapper,
            FileStorageService fileStorageService
    ) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.fileStorageService = fileStorageService;
    }

    public List<ArticleResponse> findAllPublished() {
        return articleRepository
                .findByPublishedTrueOrderByPublicationDateDesc()
                .stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    public ArticleResponse findPublishedById(Long id) {
        Article article = articleRepository
                .findByIdAndPublishedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable ou indisponible."));

        return articleMapper.toResponse(article);
    }

    public List<ArticleResponse> findAll() {
        return articleRepository.findAll().stream().map(articleMapper::toResponse).toList();
    }

    public ArticleResponse findById(Long id) {
        return articleMapper.toResponse(getArticleOrThrow(id));
    }

    @Transactional
    public ArticleResponse create(ArticleRequest request) {
        Article article = articleMapper.toEntity(request);

        if (hasImage(request.getImage())) {
            article.setImageUrl(fileStorageService.store(request.getImage(), "articles"));
        }

        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Transactional
    public ArticleResponse update(Long id, ArticleRequest request) {
        Article article = getArticleOrThrow(id);
        articleMapper.updateEntity(article, request);

        if (hasImage(request.getImage())) {
            fileStorageService.delete(article.getImageUrl());
            article.setImageUrl(fileStorageService.store(request.getImage(), "articles"));
        }

        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Transactional
    public ArticleResponse togglePublished(Long id) {
        Article article = getArticleOrThrow(id);
        article.setPublished(!article.isPublished());
        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Transactional
    public void delete(Long id) {
        Article article = getArticleOrThrow(id);
        fileStorageService.delete(article.getImageUrl());
        articleRepository.delete(article);
    }

    private boolean hasImage(MultipartFile image) {
        return image != null && !image.isEmpty();
    }

    private Article getArticleOrThrow(Long id) {
        return articleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable."));
    }
}