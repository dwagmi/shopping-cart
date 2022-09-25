package com.example.demo.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Default error handler will return 400 by default.
 */
@RestController
public class ErrorApi implements ErrorController {

    @RequestMapping(value = "/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
    }
}