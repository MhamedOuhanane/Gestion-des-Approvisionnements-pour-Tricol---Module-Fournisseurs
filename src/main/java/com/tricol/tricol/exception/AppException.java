package com.tricol.tricol.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    private final HttpStatus status;
    private final Object data;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.data = null;
    }

    public AppException(String message, HttpStatus status, Object data) {
        super(message);
        this.status = status;
        this.data = data;
    }

    public HttpStatus getStatus() { return status; }
    public Object getData() { return data; }
}
