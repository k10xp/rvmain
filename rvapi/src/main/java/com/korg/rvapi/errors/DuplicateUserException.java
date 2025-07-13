package com.korg.rvapi.errors;

//set custom message
public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
