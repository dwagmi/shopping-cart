package com.example.demo.api;

import com.example.demo.exception.ExceptionApi;
import com.example.demo.model.cart.Cart;
import com.example.demo.model.checkout.CheckoutSession;
import com.example.demo.model.exception.CartNotFoundException;
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

/**
 * REST API endpoint for the cart service.
 *
 * Exposes endpoints to view, add to, remove from, and
 * checkout shopping carts.
 *
 * Exceptions are handled centrally in {@link ExceptionApi}
 */
@RestController
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

    @GetMapping(path="/carts")
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> allCarts = cartService.getAllCarts();
        log.info("Retrieved all carts: " + allCarts);
        return ResponseEntity.status(HttpStatus.OK).body(allCarts);
    }

    @GetMapping(path="/cart/{cartId}")
    public ResponseEntity<Cart> findCartById(@PathVariable Long cartId) throws CartNotFoundException {
        Cart cart = cartService.findCartById(cartId);
        log.info("Retrieved cart: " + cart);
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @PostMapping(path="/cart/new")
    public ResponseEntity<Cart> createNewCart() {
        Cart newCart = cartService.createCart();
        return ResponseEntity.status(HttpStatus.OK).body(newCart);
    }

    /**
     * Adds/increments a product to cart with the provided quantity.
     * If the provided quantity will result in the cart having a greater
     * quantity than is available for the product, an exception will be thrown -
     * {@link com.example.demo.model.exception.QuantityUnavailableException}
     */
    @PostMapping(path="cart/{cartId}/{productId}/{quantity}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable int quantity) throws Exception {
        log.info("--- cartAPI addProductToCart ---");

        // Look up and retrieve cart from provided id.
        Cart cart = cartService.findCartById(cartId);
        log.info("Retrieved cart: " + cart);

        // Look up and retrieve product from provided id.
        Optional<Product>  optionalProduct = productService.findProductById(productId);
        Product product = optionalProduct.get();
        log.info("Retrieved product: " + product);

        // Add to cart
        Cart newCart = cartService.addProduct(cart, product, quantity);
        log.info("--- cartAPI addProductToCart --- Returning new cart after addProductToCart: " + newCart);

        return ResponseEntity.status(HttpStatus.OK).body(newCart);
    }

    /**
     * Decrements a product to cart by the provided quantity.
     * If provided quantity is greater than quantity in cart,
     * the cartItem is entirely deleted.
     */
    @DeleteMapping (path="cart/{cartId}/{productId}/{quantity}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable int quantity) throws Exception {
        log.info("--- cartAPI removeProductToCart ---");

        // Look up and retrieve cart from provided id.
        Cart cart = cartService.findCartById(cartId);
        log.info("Retrieved cart: " + cart);

        // Look up and retrieve product from provided id.
        Optional<Product>  optionalProduct = productService.findProductById(productId);
        Product product = optionalProduct.get();
        log.info("Retrieved product: " + product);

        // Decrement cart
        Cart newCart = cartService.removeProduct(cart, product, quantity);
        log.info("--- cartAPI --- Returning new cart after removeProductFromCart: " + newCart);

        return ResponseEntity.status(HttpStatus.OK).body(newCart);
    }

    // TODO: Change to POST
    @GetMapping(path="checkout/{cartId}")
    public ResponseEntity<CheckoutSession> checkout(@PathVariable Long cartId) throws CartNotFoundException {
        // Look up and retrieve cart from provided id.
        Cart cart = cartService.findCartById(cartId);
        log.info("Retrieved cart: " + cart);

        CheckoutSession checkoutSession = checkoutService.checkout(cart);

        return ResponseEntity.status(HttpStatus.OK).body(checkoutSession);
    }
}
