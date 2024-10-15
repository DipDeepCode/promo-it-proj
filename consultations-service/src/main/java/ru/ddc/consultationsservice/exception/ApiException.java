package ru.ddc.consultationsservice.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private String error;

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, String error) {
        this(message);
        this.error = error;
    }
}
