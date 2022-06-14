package com.dominik.intro.project.service;

import com.dominik.intro.project.model.ArticleDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@Component
class ArticleServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void tst(){
//        var articleDto = restTemplate.getForObject("http://jsonplaceholder.typicode.com/posts?userId=5", ArticleDto.class);
        var articleDto = restTemplate.getForObject("http://jsonplaceholder.typicode.com/posts/5", ArticleDto.class);
        System.out.println(articleDto);
    }
}