package com.example.demo.model.exception;

public class NegativeRemoveQuantityException extends Exception {
    public NegativeRemoveQuantityException(String errorMessage) {
        super(errorMessage);
    }
}
