package com.dominik.intro.project.controller;

import com.dominik.intro.project.exception.ArticleNotFoundException;
import com.dominik.intro.project.model.ArticleDto;
import com.dominik.intro.project.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class ArticleController {

    private static final Logger LOG = Logger.getLogger("Article Controller");
    @Autowired
    private ArticleService articleService;

    @SneakyThrows
    @Operation(summary = "Get article by article ID", description = "Gets the article by article ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/articles/getArticleById/{articleId}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable int articleId) {
        LOG.log(Level.INFO, "Getting article by article id: {0}", articleId);
        try {
            return ResponseEntity.ok(articleService.getArticleById(articleId));
        } catch (ArticleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @SneakyThrows
    @Operation(summary = "Get article by user ID", description = "Gets the article by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/articles/getArticleByUserId/{userId}")
    public ResponseEntity<List<ArticleDto>> getArticleByUserId(@PathVariable int userId) {
        //TODO: Get a list of articles made by a user
        var article = articleService.getArticleByUserId(userId);
        System.out.println("userId: " + userId);
        return ResponseEntity.ok(article);

//        try {
//            return ResponseEntity.ok(articleService.getArticleByUserId(userId));
//        } catch (ArticleNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
    }

    @SneakyThrows
    @Operation(summary = "Add new article", description = "Add new article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/articles/addArticle")
    public ResponseEntity<Void> addArticle(@RequestBody ArticleDto articleDto) {
        //TODO: add article to DB, if a user is present at the third party
//        var save = articleRepository.save(articleDtoToArticleEntityMapper.mapToEntity(articleDto));
//        System.out.println(save);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @SneakyThrows
    @Operation(summary = "Delete article", description = "Delete article by article Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/articles/deleteArticle/{articleId}")
    public ResponseEntity<ArticleDto> deleteArticle(@PathVariable int articleId) {
        //TODO: delete article, but only if the article was made by the same user
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @SneakyThrows
    @Operation(summary = "Update article", description = "Update existing article based on article Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/articles/updateArticle")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable int articleId, @RequestBody ArticleDto articleDto) {
        //TODO: update article, but only if the article was made by the same user
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

}
