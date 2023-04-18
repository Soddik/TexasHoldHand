package com.soddik.exception;

public class UnexpectedCardAttributeValueException extends RuntimeException{
    public UnexpectedCardAttributeValueException(String message) {
        super(message);
    }
}
