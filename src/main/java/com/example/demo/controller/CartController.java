package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {



    @RequestMapping(method = RequestMethod.GET, value="/cart")
    public HttpStatus getCart() {
        return HttpStatus.OK;
    }

    @RequestMapping(method = RequestMethod.GET, value="/products")
    public HttpStatus getProducts() {
        return HttpStatus.OK;
    }
}
