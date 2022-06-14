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

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private static final Logger LOG = Logger.getLogger("Article Service");

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleDtoToArticleEntityMapper dtoToEntityMapper;
    @Autowired
    private EntityToDtoMapper entityToDtoMapper;



    public ArticleDto getArticleById(int articleId) {

        var article = articleRepository.findById(articleId);
        if (article != null) {
            LOG.log(Level.INFO, "Found article in DB: {0}", article);
            return entityToDtoMapper.mapToDto(article);
        } else {
            var articleDto = restTemplate.getForObject("http://jsonplaceholder.typicode.com/posts/" + articleId, ArticleDto.class);
            if (articleDto == null) {
                LOG.log(Level.WARNING, "No article with article id: {0}, found in DB", articleId);
                throw new ArticleNotFoundException("Article could not be found!");
            }
            LOG.log(Level.INFO, "Found article in 3rd party DB: {0}", articleDto);

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
