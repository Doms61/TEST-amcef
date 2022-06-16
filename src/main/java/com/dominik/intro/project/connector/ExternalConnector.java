package com.dominik.intro.project.connector;

import com.dominik.intro.project.model.ArticleDto;
import com.dominik.intro.project.model.ArticleList;
import com.dominik.intro.project.model.UserDto;
import com.dominik.intro.project.model.UsersList;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Connector class for 3rd party APIs from 'http://jsonplaceholder.typicode.com'
 */
@Component
@RequiredArgsConstructor
public class ExternalConnector {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://jsonplaceholder.typicode.com";
    private static final String POST = "/posts";
    private static final String USER = "/users";

    /**
     * Get a single article
     * @param articleId articleId
     * @return ArticleDto
     */
    public ArticleDto getArticleById(int articleId) {
        return restTemplate.getForObject(BASE_URL + POST + articleId, ArticleDto.class);
    }

    /**
     * Get a list of articles
     * @param userId userId
     * @return List of ArticleDtos
     */
    public List<ArticleDto> getArticlesByUserId(int userId) {
        return restTemplate.getForObject(BASE_URL + POST +"?userId=" + userId , ArticleList.class).getArticleDtos();
    }

    /**
     * Get a list of users
     * @return List of UserDtos
     */
    public List<UserDto> getUsers() {
        return restTemplate.getForObject(BASE_URL + USER , UsersList.class).getUserDtos();
    }

    /**
     * Get a single user
     * @param userId userId
     * @return UserDto
     */
    public UserDto getUser(int userId) {
        return restTemplate.getForObject(BASE_URL + USER + userId , UserDto.class);
    }
}
