package com.soddik.exception;

public class CardAttributeException extends RuntimeException {

    public CardAttributeException(String message) {
        super(message);
    }

    public static class UnexpectedCardAttributeValueException extends RuntimeException {
        public UnexpectedCardAttributeValueException(String message) {
            super(message);
        }
    }

    public static class UnexpectedCardAttributeKindException extends RuntimeException {
        public UnexpectedCardAttributeKindException(String message) {
            super(message);
        }
    }
}