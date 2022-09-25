package com.example.demo.service.cart;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;

import java.util.List;
import java.util.Optional;

public interface BaseCartService {
    List<Cart> getAllCarts();
    Optional<Cart> findCartById(Long cartId);
    Cart addProduct(Cart cart, Product product, int quantity);
}
