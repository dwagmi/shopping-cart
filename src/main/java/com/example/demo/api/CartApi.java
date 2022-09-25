package com.example.demo.api;

import com.example.demo.exception.ExceptionApi;
import com.example.demo.model.cart.Cart;
import com.example.demo.model.checkout.CheckoutSession;
import com.example.demo.model.product.Product;
import com.example.demo.service.cart.CartService;
import com.example.demo.service.checkout.CheckoutService;
import com.example.demo.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartApi {
    private static Logger log = LoggerFactory.getLogger(CartApi.class);

    private final CartService cartService;
    private final ProductService productService;
    private final CheckoutService checkoutService;

    @Autowired
    public CartApi(CartService cartService, ProductService productService, CheckoutService checkoutService) {
        this.cartService = cartService;
        this.productService = productService;
        this.checkoutService = checkoutService;
    }

    @GetMapping(path="/all")
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> allCarts = cartService.getAllCarts();
        log.info("Retrieved all carts: " + allCarts);
        return ResponseEntity.status(HttpStatus.OK).body(allCarts);
    }

    @PostMapping(path="/new")
    public ResponseEntity<Cart> createNewCart() {
        Cart newCart = cartService.createCart();
        return ResponseEntity.status(HttpStatus.OK).body(newCart);
    }

    @GetMapping(path="/{cartId}")
    public ResponseEntity<Optional<Cart>> findCartById(@PathVariable Long cartId) {
        Optional<Cart> optionalCart = cartService.findCartById(cartId);
        log.info("Retrieved cart: " + optionalCart);
        return ResponseEntity.status(HttpStatus.OK).body(optionalCart);
    }

    // TODO: Change to POST

    /**
     * Adds a product to cart with the provided quantity. Exceptions are handled
     * centrally in {@link ExceptionApi}
     *
     * @param cartId
     * @param productId
     * @param quantity
     * @return
     */
    @GetMapping(path="/{cartId}/{productId}/{quantity}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable int quantity) {
        // Look up and retrieve cart from provided id.
        Optional<Cart> optionalCart = cartService.findCartById(cartId);
        Cart cart = optionalCart.get();
        log.info("Retrieved cart: " + cart);

        // Look up and retrieve product from provided id.
        Optional<Product>  optionalProduct = productService.findProductById(productId);
        Product product = optionalProduct.get();
        log.info("Retrieved product: " + product);

        // Add to cart
        Cart newCart = cartService.addProduct(cart, product, quantity);

        return ResponseEntity.status(HttpStatus.OK).body(newCart);
    }

    // TODO: Change to POST
    @GetMapping(path="/checkout/{cartId}")
    public ResponseEntity<CheckoutSession> checkout(@PathVariable Long cartId) {
        // Look up and retrieve cart from provided id.
        Optional<Cart> optionalCart = cartService.findCartById(cartId);
        Cart cart = optionalCart.get();
        log.info("Retrieved cart: " + cart);

        CheckoutSession checkoutSession = checkoutService.checkout(cart);

        return ResponseEntity.status(HttpStatus.OK).body(checkoutSession);
    }
}
