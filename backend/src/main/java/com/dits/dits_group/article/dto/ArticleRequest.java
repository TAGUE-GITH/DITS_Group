package com.dits.dits_group.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String summary;

    @NotBlank
    private String content;

    @NotBlank
    private String author;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publicationDate;

    private Boolean published;

    private MultipartFile image;
}