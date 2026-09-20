package com.dits.dits_group.article.controller;

import com.dits.dits_group.article.dto.ArticleResponse;
import com.dits.dits_group.article.service.ArticleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(
            ArticleService articleService
    ) {
        this.articleService = articleService;
    }

    // ==========================================
    // LISTE PUBLIQUE
    // ==========================================

    @GetMapping
    public ResponseEntity<List<ArticleResponse>>
    getPublishedArticles() {

        return ResponseEntity.ok(
                articleService.findAllPublished()
        );
    }

    // ==========================================
    // DÉTAIL PUBLIC
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse>
    getPublishedArticleById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                articleService.findPublishedById(id)
        );
    }
}