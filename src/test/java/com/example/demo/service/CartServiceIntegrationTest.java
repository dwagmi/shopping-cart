package com.example.demo.service;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.exception.CartNotFoundException;
import com.example.demo.model.exception.NegativeAddQuantityException;
import com.example.demo.model.exception.QuantityUnavailableException;
import com.example.demo.model.product.Product;
import com.example.demo.model.promotion.Promotion;
import com.example.demo.repository.product.ProductRepository;
import com.example.demo.service.cart.CartService;
import com.example.demo.service.promotion.PromotionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.example.demo.service.cart.CartService.CANNOT_ADD_NEGATIVE_AMOUNT;
import static com.example.demo.service.cart.CartService.QUANTITY_UNAVAILABLE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartServiceIntegrationTest {
    private static Logger log = LoggerFactory.getLogger(CartServiceIntegrationTest.class);

    @Autowired
    CartService cartService;

    @Autowired
    PromotionService promotionService;
    @Autowired
    ProductRepository productRepository;

    @Test
    public void givenCartService_whenCreateCart_thenReturnEmptyCart() {
        Cart newCart = cartService.createCart();
        log.info("newCart: " + newCart);

        assertTrue(newCart instanceof Cart);
        assertEquals(0, newCart.getCartItems().size());
    }

    @Test
    public void givenCartService_whenGetCart_thenShouldNotMutateCart() throws CartNotFoundException {
        Cart cart = cartService.findCartById(1L);
        Cart sameCart = cartService.findCartById(1L);

        assertEquals(cart.getCartItems().size(), sameCart.getCartItems().size());
    }

    @Test
    public void givenCartService_whenGetAllCarts_thenReturnAllCarts() {
        List<Cart> origCarts = cartService.getAllCarts();
        log.info("origCarts: " + origCarts);

        cartService.createCart();

        List<Cart> newCarts = cartService.getAllCarts();
        log.info("newCarts: " + newCarts);

        assertTrue(newCarts.get(0) instanceof Cart);
        assertEquals(origCarts.size() + 1, newCarts.size());
    }

    @Test
    public void givenCartService_whenAddProductToCart_thenReturnUpdatedCart() throws Exception {
        Cart emptyCart = cartService.createCart();
        Product product = productRepository.findById(1L).get();
        int quantity = 1;

        assertEquals(0, emptyCart.getCartItems().size());

        Cart cart = cartService.addProduct(emptyCart, product, quantity);
        log.info("cart: " + cart);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(product, cart.getCartItems().get(0).getProduct());
    }

    @Test
    public void givenCartService_whenAddPromoEligibleProductsToCart_thenReturnUpdatedCartWithPromotions() throws Exception {
        Cart emptyCart = cartService.createCart();
        List<Promotion> promotions = promotionService.findAllPromotions();
        log.info("promotions: " + promotions);

        // 3 Google Homes for the Price of 2
        Product product = productRepository.findById(1L).get();
        int quantity = 3;

        Cart cart = cartService.addProduct(emptyCart, product, quantity);
        log.info("cart: " + cart);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(1, cart.getPromotions().size());
    }

    @Test
    public void givenCartService_whenQtyToAddExceedsProductQty_thenThrowsException() throws Exception {
        Cart emptyCart = cartService.createCart();
        Product product = productRepository.findById(1L).get();

        QuantityUnavailableException exception = assertThrows(QuantityUnavailableException.class, () -> {
            cartService.addProduct(emptyCart, product, product.getQuantity() + 1);
        });

        assertEquals(QUANTITY_UNAVAILABLE, exception.getMessage());
    }

    @Test
    public void givenCartService_whenQtyToAddIsNegative_thenThrowsException() throws Exception {
        Cart emptyCart = cartService.createCart();
        Product product = productRepository.findById(1L).get();

        NegativeAddQuantityException exception = assertThrows(NegativeAddQuantityException.class, () -> {
            cartService.addProduct(emptyCart, product, -1);
        });

        assertEquals(CANNOT_ADD_NEGATIVE_AMOUNT, exception.getMessage());
    }
}
