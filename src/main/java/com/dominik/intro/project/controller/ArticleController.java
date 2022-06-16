package com.dominik.intro.project.controller;

import com.dominik.intro.project.exception.ArticleAlreadyExistsException;
import com.dominik.intro.project.exception.ArticleNotFoundException;
import com.dominik.intro.project.exception.UserNotAllowedException;
import com.dominik.intro.project.exception.UserNotFoundException;
import com.dominik.intro.project.model.ArticleDto;
import com.dominik.intro.project.service.ArticleService;
import com.dominik.intro.project.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
    @Autowired
    private UserService userService;

    @SneakyThrows
    @Operation(summary = "Get article by article ID", description = "Gets the article by article ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/articles/getArticleById/{articleId}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable int articleId) {
        LOG.log(Level.INFO, "Getting article by article id: {0}", articleId);
        try {
            return ResponseEntity.ok(articleService.getArticleById(articleId));
        } catch (ArticleNotFoundException | HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @SneakyThrows
    @Operation(summary = "Get article by user ID", description = "Gets the article by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/articles/getArticlesByUserId/{userId}")
    public ResponseEntity<List<ArticleDto>> getArticlesByUserId(@PathVariable int userId) {
        LOG.log(Level.INFO, "Getting all articles for user id: {0}", userId);

        try {
            return ResponseEntity.ok(articleService.getArticlesByUserId(userId));
        } catch (ArticleNotFoundException | HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @SneakyThrows
    @Operation(summary = "Add new article",
               description = "Add new article, permission based on existing user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "409", description = "Conflict. Article already exists"),
    })
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/articles/addArticle", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addArticle(@RequestBody ArticleDto articleDto) {

        if (userService.userHasAccess(articleDto.getUserId())) {
            try{
                articleService.saveArticle(articleDto);
            } catch (HttpClientErrorException.NotFound e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (ArticleAlreadyExistsException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @SneakyThrows
    @Operation(summary = "Delete article",
               description = "Delete article by article Id. Only a creator can delete the article.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/articles/deleteArticle/{articleId}/{userId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable int articleId, @PathVariable int userId) {
       if (!userService.userHasAccess(userId)) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
       }
        try {
            articleService.deleteArticle(articleId, userId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ArticleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @SneakyThrows
    @Operation(summary = "Update article", description = "Update existing article based on article Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/articles/updateArticle", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateArticle(@RequestBody ArticleDto articleDto) {

        if (!userService.userHasAccess(articleDto.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            articleService.updateArticle(articleDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ArticleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
