package com.lotteon.config;

public class RedirectToLoginException extends RuntimeException{
    public RedirectToLoginException(String message){
        super(message);
    }
}
