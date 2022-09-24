package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {

    private final ProductRepository productRepository;
    @Autowired
    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value="/cart")
    public HttpStatus getCart() {
        return HttpStatus.OK;
    }

    @RequestMapping(method = RequestMethod.GET, value="/products")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());

    }
}
