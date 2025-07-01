package com.juaracoding.pcmthymeleaf3.handler;


public class ErrorExceptionHandling extends RuntimeException{
    private final int statusCode;

    public ErrorExceptionHandling(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
