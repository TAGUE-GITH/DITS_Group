package com.dits.dits_group.article.controller;

import com.dits.dits_group.article.dto.ArticleRequest;
import com.dits.dits_group.article.dto.ArticleResponse;
import com.dits.dits_group.article.service.ArticleService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/articles")
public class AdminArticleController {

    private final ArticleService articleService;

    public AdminArticleController(
            ArticleService articleService
    ) {
        this.articleService = articleService;
    }

    // ==========================================
    // TOUS LES ARTICLES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<ArticleResponse>>
    getAllArticles() {

        return ResponseEntity.ok(
                articleService.findAll()
        );
    }

    // ==========================================
    // DÉTAIL ADMIN
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse>
    getArticleById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                articleService.findById(id)
        );
    }

    // ==========================================
    // CRÉER
    // ==========================================

    @PostMapping
    public ResponseEntity<ArticleResponse>
    createArticle(
            @Valid
            @RequestBody ArticleRequest request
    ) {

        ArticleResponse article =
                articleService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(article);
    }

    // ==========================================
    // MODIFIER
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse>
    updateArticle(
            @PathVariable Long id,

            @Valid
            @RequestBody ArticleRequest request
    ) {

        return ResponseEntity.ok(
                articleService.update(
                        id,
                        request
                )
        );
    }

    // ==========================================
    // PUBLIER / METTRE EN BROUILLON
    // ==========================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<ArticleResponse>
    toggleArticleStatus(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                articleService.togglePublished(id)
        );
    }

    // ==========================================
    // SUPPRIMER
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>>
    deleteArticle(
            @PathVariable Long id
    ) {

        articleService.delete(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Article supprimé avec succès."
                )
        );
    }
}