package com.dominik.intro.project.exception;

public class ArticleAlreadyExistsException extends RuntimeException{

    public ArticleAlreadyExistsException() {
    }
    public ArticleAlreadyExistsException(String message) {
        super(message);
    }

}
