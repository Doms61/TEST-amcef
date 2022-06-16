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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService service;
    @Mock
    private ArticleRepository repository;
//    @Mock
    @Mock
    private ExternalConnector connector;
    @Spy
    private ArticleDtoToArticleEntityMapper dtoToArticleEntityMapper;
    @Spy
    private EntityToDtoMapper entityToDtoMapper;

    @Test
    void getArticleById_FromExternal(){

        doReturn(Optional.of(Article.builder().build())).when(repository).findById(any());

        doReturn(ArticleDto.builder()
                .userId(1)
                .id(5)
                .title("Title")
                .body("Body")
                .build()).when(connector).getArticleById(anyInt());

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

        doReturn(Optional.of(Article.builder()
                .userId(1)
                .id(5)
                .title("Title")
                .body("Body")
                .build())).when(repository).findById(anyInt());

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

        doReturn(Optional.of(Article.builder().build())).when(repository).findById(anyInt());
        doReturn(ArticleDto.builder().build()).when(connector).getArticleById(anyInt());

        assertThrows(ArticleNotFoundException.class, () -> service.getArticleById(5));
    }

    @Test
    void getArticlesByUserId_FromExternal(){

        doReturn(Collections.EMPTY_LIST).when(repository).findAllByUserId(anyInt());
        doReturn(List.of(ArticleDto.builder()
                .userId(1)
                .id(5)
                .title("Title")
                .body("Body")
                .build())).when(connector).getArticlesByUserId(anyInt());

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
                .build())).when(repository).findAllByUserId(anyInt());

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

        doReturn(Collections.EMPTY_LIST).when(repository).findAllByUserId(anyInt());
        doReturn(Collections.EMPTY_LIST).when(connector).getArticlesByUserId(anyInt());

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

        doReturn(Optional.of(article)).when(repository).findById(anyInt());

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

        doReturn(Optional.empty()).when(repository).findById(anyInt());

        assertDoesNotThrow(() -> service.saveArticle(article));
    }

    @Test
    void deleteArticle_NotFound(){

        doReturn(Optional.empty()).when(repository).findById(anyInt());
        assertThrows(ArticleNotFoundException.class, () -> service.deleteArticle(1,1));
    }

    @Test
    void deleteArticle_NotAllowed(){
        doReturn(Optional.of(Article.builder()
                        .userId(5)
                        .id(1)
                        .title("title")
                        .body("body")
                        .build())
                ).when(repository).findById(anyInt());
        assertThrows(UserNotAllowedException.class, () -> service.deleteArticle(1,1));
    }

    @Test
    void deleteArticle(){
        doReturn(Optional.of(Article.builder()
                .userId(1)
                .id(1)
                .title("title")
                .body("body")
                .build())
        ).when(repository).findById(anyInt());

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
        doReturn(Optional.empty()).when(repository).findById(anyInt());
        assertThrows(ArticleNotFoundException.class, () -> service.updateArticle(articleDto));
    }

    @Test
    void updateArticle_NotAllowed(){
        var articleDto = ArticleDto.builder()
                .userId(4)
                .id(1)
                .title("title")
                .body("body")
                .build();

        doReturn(Optional.of(Article.builder()
                .userId(5)
                .id(1)
                .title("title")
                .body("body")
                .build())
        ).when(repository).findById(anyInt());
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

        doReturn(Optional.of(Article.builder()
                .userId(5)
                .id(1)
                .title("title")
                .body("body")
                .build())
        ).when(repository).findById(anyInt());

        assertDoesNotThrow(() -> service.updateArticle(articleDto));
    }
}