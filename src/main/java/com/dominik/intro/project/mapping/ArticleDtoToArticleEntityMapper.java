package com.dominik.intro.project.mapping;

import com.dominik.intro.project.db.entity.Article;
import com.dominik.intro.project.model.ArticleDto;
import org.springframework.stereotype.Component;

@Component
public class ArticleDtoToArticleEntityMapper {

    public Article mapToEntity(ArticleDto articleDto) {
        return new Article().builder()
                .id(articleDto.getId())
                .body(articleDto.getBody())
                .title(articleDto.getTitle())
                .userId(articleDto.getUserId())
                .build();
    }
}
