package com.example.demo.repository;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.cart.CartItem;
import com.example.demo.model.product.Product;
import com.example.demo.repository.cart.CartItemRepository;
import com.example.demo.repository.cart.CartRepository;
import com.example.demo.repository.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration testing for the CartRepository
 *
 * Uses an in-memory H2 relational database for the tests
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartRepositoryIntegrationTest {
    private static Logger log = LoggerFactory.getLogger(CartRepositoryIntegrationTest.class);

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void givenCartRepository_whenSaveEmptyCart_thenRetrieveCart() {
        Cart cart = cartRepository.save(new Cart());
        Optional<Cart> foundCart = cartRepository.findById(cart.getId());

        assertTrue(foundCart.isPresent());
        assertEquals(cart.getId(), foundCart.get().getId());
    }

    @Test
    public void givenCartRepository_whenSaveNonEmptyCart_thenRetrieveCart() {
        Cart cart = cartRepository.save(new Cart());
        Product product = productRepository.findById(1L).get();
        int qty = 1;
        log.info("Saving " + qty + " of " + product + " to cart " + cart);

        cartItemRepository.save(new CartItem(cart, product, qty));

        Optional<Cart> foundCart = cartRepository.findById(cart.getId());
        log.info("foundCart: " + foundCart);

        assertTrue(foundCart.isPresent());
        assertEquals(cart.getId(), foundCart.get().getId());

    }

}
