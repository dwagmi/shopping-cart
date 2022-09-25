package com.example.demo.api;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartApi {
    Logger log = LoggerFactory.getLogger(CartApi.class);

    private final CartService cartService;

    @Autowired
    public CartApi(CartService cartService) {
        this.cartService = cartService;
    }

    @RequestMapping(method = RequestMethod.GET, value="/list")
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> allCarts = cartService.getAllCarts();
        log.info("Retrieved all carts: " + allCarts);
        return ResponseEntity.status(HttpStatus.OK).body(allCarts);
    }


}
