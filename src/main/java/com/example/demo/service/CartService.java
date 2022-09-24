package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements BaseCartService {
    Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart getCartById(Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            return optionalCart.get();
        }

        Cart cart = cartRepository.save(new Cart());
        return cart;
    }

    public Cart addProduct(Cart cart, Product product, int quantity) {
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty() || (!cartItems.contains(product) && quantity <= product.getQuantity())) {
            cartItems.add(new CartItem(product, quantity));
        }
        cartRepository.save(cart);
        return cart;
    }
}
