package com.soddik.exception;

public class UnexpectedCardAttribute extends RuntimeException {
    public UnexpectedCardAttribute(String message) {
        super(message);
    }
}