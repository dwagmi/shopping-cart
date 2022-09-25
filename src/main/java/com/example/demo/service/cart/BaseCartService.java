package com.example.demo.service.cart;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.exception.CartNotFoundException;
import com.example.demo.model.product.Product;

import java.util.List;

public interface BaseCartService {
    List<Cart> getAllCarts();

    Cart findCartById(Long cartId) throws CartNotFoundException;

    Cart addProduct(Cart cart, Product product, int quantity) throws Exception;
}
