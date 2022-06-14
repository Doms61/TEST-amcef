package com.dominik.intro.project.connector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dominik.intro.project.model.ArticleDto;
import com.dominik.intro.project.model.UserDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExternalConnector {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://jsonplaceholder.typicode.com";
    private static final String POST = "/posts";
    private static final String USER = "/users";

    public ArticleDto getArticleById(int articleId) {
        return restTemplate.getForObject(BASE_URL + POST + articleId, ArticleDto.class);
    }

    public List<ArticleDto> getArticlesByUserId(int userId) {

        var forObject = restTemplate.getForObject("http://jsonplaceholder.typicode.com/posts?userId=" + userId, ArticleDto.class);
        System.out.println("For Object: " + forObject);
        return null;
    }

    public List<UserDto> getUsers() {

        return null;
    }
}
