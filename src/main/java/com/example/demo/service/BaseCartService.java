package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;

import java.util.List;

public interface BaseCartService {
    List<Cart> getAllCarts();

    Cart getCartById(Long cartId);
    Cart addProduct(Cart cart, Product product, int quantity);
}
