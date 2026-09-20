package com.dits.dits_group.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequest {

    @NotBlank(
            message = "Le titre de l'article est obligatoire."
    )
    @Size(
            max = 200,
            message = "Le titre ne peut pas dépasser 200 caractères."
    )
    private String title;

    @NotBlank(
            message = "Le résumé de l'article est obligatoire."
    )
    @Size(
            max = 500,
            message = "Le résumé ne peut pas dépasser 500 caractères."
    )
    private String summary;

    @NotBlank(
            message = "Le contenu de l'article est obligatoire."
    )
    private String content;

    @Size(
            max = 500,
            message = "L'URL de l'image est trop longue."
    )
    private String imageUrl;

    @NotBlank(
            message = "L'auteur est obligatoire."
    )
    @Size(
            max = 150,
            message = "Le nom de l'auteur ne peut pas dépasser 150 caractères."
    )
    private String author;

    private LocalDateTime publicationDate;

    private Boolean published;
}