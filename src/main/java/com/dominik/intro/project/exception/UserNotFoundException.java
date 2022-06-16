package com.dominik.intro.project.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
