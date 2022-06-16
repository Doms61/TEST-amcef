package com.dominik.intro.project.service;

import java.util.List;

import com.dominik.intro.project.connector.ExternalConnector;
import com.dominik.intro.project.db.entity.Article;
import com.dominik.intro.project.db.repo.ArticleRepository;
import com.dominik.intro.project.exception.ArticleAlreadyExistsException;
import com.dominik.intro.project.exception.ArticleNotFoundException;
import com.dominik.intro.project.exception.UserNotAllowedException;
import com.dominik.intro.project.model.ArticleDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private ArticleRepository repository;
    private ArticleService service;
    @Mock
    private ExternalConnector connector;

    @Test
    void getArticleById_FromExternal(){

        doReturn(null).when(repository).findById(any());
        doReturn(ArticleDto.builder()
                .userId(1)
                .id(5)
                .title("Title")
                .body("Body")
                .build()).when(connector).getArticleById(any());

        var response = service.getArticleById(5);
        assertAll(
                () -> assertEquals("Title", response.getTitle()),
                () -> assertEquals("Body", response.getBody()),
                () -> assertEquals(5, response.getId()),
                () -> assertEquals(1, response.getUserId())
        );
    }

    @Test
    void getArticleById_FromInternal(){

        doReturn(Article.builder()
                .userId(1)
                .id(5)
                .title("Title")
                .body("Body")
                .build()).when(repository).findById(any());

        var response = service.getArticleById(5);
        assertAll(
                () -> assertEquals("Title", response.getTitle()),
                () -> assertEquals("Body", response.getBody()),
                () -> assertEquals(5, response.getId()),
                () -> assertEquals(1, response.getUserId())
        );
    }

    @Test
    void getArticleById_NotFound(){

        doReturn(null).when(repository).findById(any());
        doReturn(null).when(connector).getArticleById(any());

        assertThrows(ArticleNotFoundException.class, () -> service.getArticleById(5));
    }

    @Test
    void getArticlesByUserId_FromExternal(){

        doReturn(null).when(repository).findAllByUserId(any());
        doReturn(List.of(ArticleDto.builder()
                .userId(1)
                .id(5)
                .title("Title")
                .body("Body")
                .build())).when(connector).getArticlesByUserId(any());

        var response = service.getArticlesByUserId(5);
        assertAll(
                () -> assertEquals("Title", response.get(0).getTitle()),
                () -> assertEquals("Body", response.get(0).getBody()),
                () -> assertEquals(5, response.get(0).getId()),
                () -> assertEquals(1, response.get(0).getUserId())
        );
    }

    @Test
    void getArticlesByUserId_FromInternal(){

        doReturn(List.of(Article.builder()
                .userId(1)
                .id(5)
                .title("Title")
                .body("Body")
                .build())).when(repository).findAllByUserId(any());

        var response = service.getArticlesByUserId(5);
        assertAll(
                () -> assertEquals("Title", response.get(0).getTitle()),
                () -> assertEquals("Body", response.get(0).getBody()),
                () -> assertEquals(5, response.get(0).getId()),
                () -> assertEquals(1, response.get(0).getUserId())
        );
    }

    @Test
    void getArticlesByUserId_NotFound(){

        doReturn(null).when(repository).findAllByUserId(any());
        doReturn(null).when(connector).getArticlesByUserId(any());

        assertThrows(ArticleNotFoundException.class, () -> service.getArticlesByUserId(5));
    }

    @Test
    void saveArticle_AlreadyExists() {

        var article = ArticleDto.builder()
                .userId(1)
                .id(1)
                .title("title")
                .body("Body")
                .build();

        doReturn(article).when(repository).findById(any());

        assertThrows(ArticleAlreadyExistsException.class, () -> service.saveArticle(article));
    }

    @Test
    void saveArticle() {

        var article = ArticleDto.builder()
                .userId(1)
                .id(1)
                .title("title")
                .body("Body")
                .build();

        doReturn(null).when(repository).save(any());
        doReturn(null).when(repository).findById(any());

        assertDoesNotThrow(() -> service.saveArticle(article));
    }

    @Test
    void deleteArticle_NotFound(){

        doReturn(null).when(repository).findById(any());
        assertThrows(ArticleNotFoundException.class, () -> service.deleteArticle(1,1));
    }

    @Test
    void deleteArticle_NotAllowed(){
        doReturn(Article.builder()
                        .userId(5)
                        .id(1)
                        .title("title")
                        .body("body")
                        .build()
                ).when(repository).findById(any());
        assertThrows(UserNotAllowedException.class, () -> service.deleteArticle(1,1));
    }

    @Test
    void deleteArticle(){
        doReturn(Article.builder()
                .userId(1)
                .id(1)
                .title("title")
                .body("body")
                .build()
        ).when(repository).findById(any());
        doNothing().when(repository).delete(any());

        assertDoesNotThrow(() -> service.deleteArticle(1,1));
    }

    @Test
    void updateArticle_NotFound(){

        var articleDto = ArticleDto.builder()
                .userId(5)
                .id(1)
                .title("title")
                .body("body")
                .build();
        doReturn(null).when(repository).findById(any());
        assertThrows(ArticleNotFoundException.class, () -> service.updateArticle(articleDto));
    }

    @Test
    void updateArticle_NotAllowed(){
        var articleDto = ArticleDto.builder()
                .userId(5)
                .id(1)
                .title("title")
                .body("body")
                .build();

        doReturn(Article.builder()
                .userId(5)
                .id(1)
                .title("title")
                .body("body")
                .build()
        ).when(repository).findById(any());
        assertThrows(UserNotAllowedException.class, () -> service.updateArticle(articleDto));
    }

    @Test
    void updateArticle(){
        var articleDto = ArticleDto.builder()
                .userId(5)
                .id(1)
                .title("title")
                .body("body")
                .build();

        doReturn(Article.builder()
                .userId(1)
                .id(1)
                .title("title")
                .body("body")
                .build()
        ).when(repository).findById(any());
        doNothing().when(repository).delete(any());

        assertDoesNotThrow(() -> service.updateArticle(articleDto));
    }
}