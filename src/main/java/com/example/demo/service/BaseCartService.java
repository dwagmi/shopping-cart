package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;

import java.util.List;
import java.util.Optional;

public interface BaseCartService {
    List<Cart> getAllCarts();

    Optional<Cart> findCartById(Long cartId);
    Cart addProduct(Cart cart, Product product, int quantity);
}
