package com.lotteon.security;

public class InactiveUserException extends  RuntimeException{
    public InactiveUserException(String message) {
        super(message);
    }
}
