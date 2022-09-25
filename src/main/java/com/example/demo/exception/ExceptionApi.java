package com.example.demo.exception;

import com.example.demo.model.exception.*;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

import static com.example.demo.service.cart.CartService.*;

/**
 * Default error handler will return 400 by default.
 */
@RestController
@ControllerAdvice
public class ExceptionApi extends ResponseEntityExceptionHandler implements ErrorController {

    /**
     * Default error handling, used as a fall-back if no specific error handler
     * exists for a particular Exception.
     *
     * @return
     */
    @RequestMapping(value = "/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Bad request: Input is in the wrong format or the requested quantity is unavailable");
    }

    /**
     * Handles type mismatches in path variables of API request.
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
    protected ResponseEntity<Object> handleConflict(
            RuntimeException e, WebRequest request) {
        String bodyOfResponse = "Input format is incorrect";
        return handleExceptionInternal(e, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Handles NoSuchElementExceptions
     */
    @ExceptionHandler(value = { NoSuchElementException.class })
    protected ResponseEntity<Object> handleNoSuchElementException(
            RuntimeException e, WebRequest request) {
        String bodyOfResponse = "No Such Element Exists";
        return handleExceptionInternal(e, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Handles QuantityUnavailableException
     */
    @ExceptionHandler(value = { QuantityUnavailableException.class })
    public ResponseEntity<String> handleQuantityUnavailableException() {
        return ResponseEntity.status(400).body(QUANTITY_UNAVAILABLE);
    }

    /**
     * Handles NegativeAddQuantityException
     */
    @ExceptionHandler(value = { NegativeAddQuantityException.class })
    public ResponseEntity<String> handleNegativeAddQuantityException() {
        return ResponseEntity.status(400).body(CANNOT_ADD_NEGATIVE_AMOUNT);
    }

    /**
     * Handles NegativeRemoveQuantityException
     */
    @ExceptionHandler(value = { NegativeRemoveQuantityException.class })
    public ResponseEntity<String> handleNegativeRemoveQuantityException() {
        return ResponseEntity.status(400).body(CANNOT_REMOVE_NEGATIVE_AMOUNT);
    }

    /**
     * Handles ProductNotFoundException
     */
    @ExceptionHandler(value = { ProductNotFoundException.class })
    public ResponseEntity<String> handleProductNotFoundException() {
        return ResponseEntity.status(404).body(PRODUCT_NOT_FOUND_IN_CART);
    }

    /**
     * Handles CartNotFoundException
     */
    @ExceptionHandler(value = { CartNotFoundException.class })
    public ResponseEntity<String> handleCartNotFoundException() {
        return ResponseEntity.status(404).body(CART_NOT_FOUND);
    }
}