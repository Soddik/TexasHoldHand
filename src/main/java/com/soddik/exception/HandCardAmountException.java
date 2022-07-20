package com.soddik.exception;

public class HandCardAmountException extends RuntimeException{
    public HandCardAmountException(String message) {
        super(message);
    }

    public static class UniqueCardException extends RuntimeException{
        public UniqueCardException(String message) {
            super(message);
        }
    }
}