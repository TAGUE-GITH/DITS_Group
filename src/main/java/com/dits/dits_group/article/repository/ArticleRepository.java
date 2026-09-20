package com.dits.dits_group.article.repository;

import com.dits.dits_group.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository
        extends JpaRepository<Article, Long> {

    // Articles publics, du plus récent au plus ancien
    List<Article> findByPublishedTrueOrderByPublicationDateDesc();

    // Détail public : uniquement si publié
    Optional<Article> findByIdAndPublishedTrue(Long id);
}