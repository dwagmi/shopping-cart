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
        return cartRepository.findAllByOrderByIdAsc();
    }

    /**
     * Returns a cart with the provided id if it exists, otherwise returns null
     *
     * @param cartId
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Optional<Cart> findCartById(Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        return optionalCart;
    }

    /**
     * Creates a new cart and returns the new, empty cart.
     *
     * @return
     */
    public Cart createCart() {
        return cartRepository.save(new Cart());
    }

    public Cart addProduct(Cart cart, Product product, int quantity) {
        log.info("Adding " + quantity + " of product " + product + " to " + cart);
        List<CartItem> cartItems = cart.getCartItems();
        log.info("Current cartItems: " + cartItems);
        if (cartItems.isEmpty() || (!cartItems.contains(product) && quantity <= product.getQuantity())) {
            log.info("Item is OK to add");
            cartItemRepository.save(new CartItem(cart, product, quantity));
            cart = cartRepository.findById(cart.getId()).get();
            log.info("New cartItems: " + cartItems);
            log.info("New cart: " + cart);
        } else {
            log.info ("Nothing to save");
        }
        return cart;
    }
}
