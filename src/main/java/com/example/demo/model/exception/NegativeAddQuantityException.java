package com.example.demo.model.exception;

public class NegativeAddQuantityException extends Exception {
    public NegativeAddQuantityException(String errorMessage) {
        super(errorMessage);
    }
}
