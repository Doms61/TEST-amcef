package com.dominik.intro.project.controller;

import com.doms.articles.ApiException;
import com.doms.articles.api.DefaultApi;
import com.doms.articles.models.ArticleResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
//@NoArgsConstructor
public class ArticlesController extends DefaultApi {

    @Override
    public ArticleResponse getArticleById(Integer articleId) throws ApiException {
        return super.getArticleById(articleId);
    }
}
