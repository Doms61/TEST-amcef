package com.dominik.intro.project.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class ArticleList {
    private List<ArticleDto> articleDtos;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public ArticleList(List<ArticleDto> articleDtos) {
        this.articleDtos = articleDtos;
    }
}
