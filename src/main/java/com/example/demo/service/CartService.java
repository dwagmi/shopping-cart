package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.CheckoutSession;
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
import java.util.stream.Collectors;

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

        // Checks if product already exists in the cart, if it does, update the quantity
        Optional<CartItem> optionalCartItem = cart.getCartItems().stream().filter(item -> item.getProduct().equals(product)).findFirst();
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return cartRepository.findById(cart.getId()).get();
        }


        if (isOkToAdd(cart, product, quantity)) {
            log.info("Item is OK to add");

            // Saves the new cartItem and then retrieves the updated cart
            cartItemRepository.save(new CartItem(cart, product, quantity));
            Cart newCart = cartRepository.findById(cart.getId()).get();

            log.info("New cartItems: " + newCart.getCartItems());
            log.info("New cart: " + newCart);
            return newCart;
        } else {
            log.info ("Nothing to add");
            return cart;
        }
    }

    /**
     * Validates whether it is Ok to add the requested quantity of product to
     * the cart.
     *
     * TODO: Throw custom exceptions to be handled and returned by REST endpoint.
     *
     * @param cart
     * @param product
     * @param quantity
     * @return
     */
    private boolean isOkToAdd(Cart cart, Product product, int quantity) {
        log.info("Current cartItems: " + cart.getCartItems());
        log.info("Current products: " + cart.getProducts());

        List<CartItem> cartItems = cart.getCartItems();
        List<Product> productsInCart = cart.getProducts();

        // Quantity requested to be added to cart exceeds product availability
        if (quantity > product.getQuantity()) {
            return false;
        }

        // Checks if product already exists in the cart
        Optional<CartItem> optionalCartItem = cartItems.stream().filter(item -> item.getProduct().equals(product)).findFirst();
        if (optionalCartItem.isPresent()) {
                CartItem cartItem = optionalCartItem.get();
                // Ensures that the resulting total quantity does not exceed product availability
                if (cartItem.getQuantity() + quantity > product.getQuantity()) {
                    return false;
                }
        }

        // Resulting quantity of product in cart after adding the new amount exceeds product availability
        if (cartItems.isEmpty() || !cartItems.contains(product)) {
            return true;
        } else {
            return false;
        }
    }

    public CheckoutSession checkout(Cart cart) {

        return new CheckoutSession();
    }
}
