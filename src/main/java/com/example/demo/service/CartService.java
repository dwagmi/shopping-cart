package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements BaseCartService {
    Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Cart getCartById(Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            return optionalCart.get();
        }
        Cart cart = cartRepository.save(new Cart());
        log.info("Created a new cart with id: " + cart.getId());
        return cart;
    }

    public Cart addProduct(Cart cart, Product product, int quantity) {
        log.info("Adding " + quantity + " of product " + product + " to " + cart);
        List<CartItem> cartItems = cart.getCartItems();
        log.info("Current cartItems: " + cartItems);
        if (cartItems.isEmpty() || (!cartItems.contains(product) && quantity <= product.getQuantity())) {
            cartItemRepository.save(new CartItem(cart, product, quantity));
//            cartItems.add(new CartItem(product, quantity));
//            log.info("New cartItems: " + cartItems);
//            log.info("New cart: " + cart);
//            cartRepository.save(cart);
        } else {
            log.info ("Nothing to save");
        }
        return cart;
    }
}
