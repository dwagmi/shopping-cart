package com.example.demo.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;


/**
 * Default error handler will return 400 by default.
 */
@RestController
@ControllerAdvice
public class ErrorApi extends ResponseEntityExceptionHandler implements ErrorController {

    /**
     * Default error handling, used as a fall-back if no specific error handler
     * exists for a particular Exception.
     *
     * @return
     */
    @RequestMapping(value = "/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
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
     * Handles requested resource is not found.
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = { NoSuchElementException.class })
    protected ResponseEntity<Object> handleNoSuchElementException(
            RuntimeException e, WebRequest request) {
        String bodyOfResponse = "Resource cannot be found";
        return handleExceptionInternal(e, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}