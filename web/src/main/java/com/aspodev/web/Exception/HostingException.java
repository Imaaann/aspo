package com.aspodev.web.Exception;

public class HostingException extends RuntimeException {
    public HostingException(String message) {
        super(message);
    }

    public HostingException(String message, Throwable cause) {
        super(message, cause);
    }
}