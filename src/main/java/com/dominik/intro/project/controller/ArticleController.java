package com.dominik.intro.project.controller;

import com.dominik.intro.project.model.Article;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ArticleController {

    @Operation(summary = "Get article by article ID", description = "Gets the article by article ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/articles/getArticleById/{articleId}")
    public ResponseEntity<Article> getArticleById(@PathVariable int articleId) {
        return ResponseEntity.ok(new Article());//status(HttpStatus.NOT_IMPLEMENTED);
    }
}
