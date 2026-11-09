package com.github.sambhavmahajan.wakasa.exception;

public class TokenAlreadyExistsException extends RuntimeException{
    public TokenAlreadyExistsException(String message) {
        super(message);
    }
}
