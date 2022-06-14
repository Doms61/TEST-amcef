package com.dominik.intro.project.service;

import com.dominik.intro.project.db.repo.ArticleRepository;
import com.dominik.intro.project.exception.ArticleNotFoundException;
import com.dominik.intro.project.mapping.ArticleDtoToArticleEntityMapper;
import com.dominik.intro.project.mapping.EntityToDtoMapper;
import com.dominik.intro.project.model.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ArticleService {

    @Autowired
    private RestTemplate restTemplate;
    private ArticleRepository articleRepository;
    private ArticleDtoToArticleEntityMapper dtoToEntityMapper;
    private EntityToDtoMapper entityToDtoMapper;



    public ArticleDto getArticleById(int articleId) {

        var article = articleRepository.findById(articleId);
        if (article != null) {
            return entityToDtoMapper.mapToDto(article);
        } else {
            var articleDto = restTemplate.getForObject("http://jsonplaceholder.typicode.com/posts/" + articleId, ArticleDto.class);
            if (articleDto == null) {
                throw new ArticleNotFoundException("Article could not be found!");
            }
            articleRepository.save(dtoToEntityMapper.mapToEntity(articleDto));
            return articleDto;
        }



    }

    //TODO: fix it for all articles
    public ArticleDto getArticleByUserId(int userId) {
        var article = articleRepository.findById(userId);
        if (article != null) {
            return entityToDtoMapper.mapToDto(article);
        } else {
            var articleDto = restTemplate.getForObject("http://jsonplaceholder.typicode.com/posts?userId=" + userId, ArticleDto.class);
            if (articleDto == null) {
                throw new ArticleNotFoundException("Article could not be found!");
            }
            articleRepository.save(dtoToEntityMapper.mapToEntity(articleDto));
            return articleDto;
        }
    }
}
