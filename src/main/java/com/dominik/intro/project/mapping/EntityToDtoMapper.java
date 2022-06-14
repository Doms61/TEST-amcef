package com.dominik.intro.project.mapping;

import com.dominik.intro.project.db.entity.Article;
import com.dominik.intro.project.model.ArticleDto;
import org.springframework.stereotype.Component;

@Component
public class EntityToDtoMapper {

    public ArticleDto mapToDto(Article article) {
        return new ArticleDto().builder()
                .id(article.getId())
                .userId(article.getUserId())
                .body(article.getBody())
                .title(article.getTitle())
                .build();
    }
}
