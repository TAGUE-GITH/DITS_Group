package com.dits.dits_group.article.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleResponse {

    private Long id;

    private String title;

    private String summary;

    private String content;

    private String imageUrl;

    private String author;

    private LocalDateTime publicationDate;

    private boolean published;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}