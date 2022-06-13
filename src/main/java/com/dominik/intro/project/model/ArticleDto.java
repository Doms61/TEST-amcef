package com.dominik.intro.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private int id;
    private int userId;
    private String title;
    private String body;
}
