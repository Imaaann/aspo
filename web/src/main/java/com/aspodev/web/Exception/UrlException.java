package com.aspodev.web.Exception;

public class UrlException extends RuntimeException{
    private String message;
    UrlException(String message){
        super();
        this.message = message;
    }
}
