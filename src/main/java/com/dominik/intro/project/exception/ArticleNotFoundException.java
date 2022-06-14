package com.dominik.intro.project.exception;

public class ArticleNotFoundException extends RuntimeException{

    public ArticleNotFoundException() {
    }
    public ArticleNotFoundException(String message) {
        super(message);
    }
}
