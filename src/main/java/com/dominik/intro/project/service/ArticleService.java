package com.dominik.intro.project.service;

import com.dominik.intro.project.connector.ExternalConnector;
import com.dominik.intro.project.db.entity.Article;
import com.dominik.intro.project.db.repo.ArticleRepository;
import com.dominik.intro.project.exception.ArticleAlreadyExistsException;
import com.dominik.intro.project.exception.ArticleNotFoundException;
import com.dominik.intro.project.exception.UserNotAllowedException;
import com.dominik.intro.project.mapping.ArticleDtoToArticleEntityMapper;
import com.dominik.intro.project.mapping.EntityToDtoMapper;
import com.dominik.intro.project.model.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private static final Logger LOG = Logger.getLogger("Article Service");

    @Autowired
    private ExternalConnector connector;
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
            var articleDto = connector.getArticleById(articleId);
            if (articleDto == null) {
                LOG.log(Level.WARNING, "No article with article id: {0}, found in DB", articleId);
                throw new ArticleNotFoundException("Article could not be found!");
            }
            LOG.log(Level.INFO, "Found article in 3rd party DB: {0}", articleDto);

            articleRepository.save(dtoToEntityMapper.mapToEntity(articleDto));
            return articleDto;
        }
    }

    public List<ArticleDto> getArticlesByUserId(int userId) {

        var articles = articleRepository.findAllByUserId(userId);
        if (!articles.isEmpty()) {
            List<ArticleDto> list = new ArrayList<>();
            LOG.log(Level.INFO, "Found {0} articles in DB for user", articles.size());
            articles.forEach(article -> list.add(entityToDtoMapper.mapToDto(article)));
            return list;
        } else {
            var articlesDto = connector.getArticlesByUserId(userId);
            if (articlesDto.isEmpty()) {
                LOG.log(Level.WARNING, "No articles found for user id: {0}", userId);
                throw new ArticleNotFoundException("No article could not be found!");
            }
            LOG.log(Level.INFO, "Found {0} articles in 3rd party DB", articlesDto.size());

            articlesDto.forEach(articleDto -> articleRepository.save(dtoToEntityMapper.mapToEntity(articleDto)));
            return articlesDto;
        }
    }

    public void saveArticle(ArticleDto articleDto) {

        if (articleRepository.findById(articleDto.getId()) != null) {
            throw new ArticleAlreadyExistsException("This article already exists in the database.");
        }
        LOG.log(Level.INFO, "Saving article with ID: {0}, to DB", articleDto.getId());
        articleRepository.save(dtoToEntityMapper.mapToEntity(articleDto));
    }

    public void deleteArticle(int articleId, int userId) {

        var article = articleRepository.findById(articleId);
        if (article == null) {
            throw new ArticleNotFoundException("Article was not found for deletion.");
        }
        if (article.getUserId() != userId){
            throw new UserNotAllowedException("The user is not allowed to delete this article.");
        }
        articleRepository.delete(article);

    }

    public void updateArticle(ArticleDto articleDto) {

        var article = articleRepository.findById(articleDto.getId());
        if (article == null) {
            throw new ArticleNotFoundException("Article was not found for deletion.");
        }
        if (article.getUserId() != articleDto.getUserId()){
            throw new UserNotAllowedException("The user is not allowed to delete this article.");
        }
        articleRepository.save(dtoToEntityMapper.mapToEntity(articleDto));
    }
}
