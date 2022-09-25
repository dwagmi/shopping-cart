package com.example.demo.service.cart;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.cart.CartItem;
import com.example.demo.model.exception.*;
import com.example.demo.model.product.Product;
import com.example.demo.model.promotion.Promotion;
import com.example.demo.repository.cart.CartItemRepository;
import com.example.demo.repository.cart.CartRepository;
import com.example.demo.service.promotion.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements BaseCartService {
    public static final String QUANTITY_UNAVAILABLE = "Quantity unavailable";
    public static final String CANNOT_ADD_NEGATIVE_AMOUNT = "Cannot add negative amount";
    public static final String PRODUCT_NOT_FOUND_IN_CART = "Product not found in cart";
    public static final String CANNOT_REMOVE_NEGATIVE_AMOUNT = "Cannot remove negative amount";
    public static final String CART_NOT_FOUND = "Cart not found";
    private static Logger log = LoggerFactory.getLogger(CartService.class);

    private final PromotionService promotionService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(PromotionService promotionService, CartRepository cartRepository,
            CartItemRepository cartItemRepository) {
        this.promotionService = promotionService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * For demo purposes only
     */
    public List<Cart> getAllCarts() {
        return cartRepository.findAllByOrderByIdAsc();
    }

    /**
     * Returns a cart with the provided id if it exists, otherwise returns null
     */
    @Override
    public Cart findCartById(Long cartId) throws CartNotFoundException {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            return updateWithPromotions(optionalCart.get());
        } else {
            throw new CartNotFoundException(CART_NOT_FOUND);
        }
    }

    /**
     * Creates a new cart and returns the new empty cart.
     */
    public Cart createCart() {
        return cartRepository.save(new Cart());
    }

    /**
     * Adds product to cart or update if already in cart. Then search and refresh
     * promotions
     * applicable to cart.
     */
    public Cart addProduct(Cart cart, Product product, int quantity) throws Exception {
        log.info("--- addProduct --- Adding " + quantity + " of product " + product + " to " + cart);

        if (quantity < 1)
            throw new NegativeAddQuantityException(CANNOT_ADD_NEGATIVE_AMOUNT);

        Optional<CartItem> optionalCartItem = cart.contains(product);
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            log.info("Product already in cart, updating quantity only");

            if (cartItem.getQuantity() + quantity > product.getQuantity()) {
                throw new QuantityUnavailableException(QUANTITY_UNAVAILABLE);
            }

            updateExistingCartItemQuantity(cartItem, cartItem.getQuantity() + quantity);
        } else {
            addNewCartItemWithQuantity(cart, product, quantity);
            log.info("Added new product to cart");
        }

        Cart newCart = findCartById(cart.getId());
        return updateWithPromotions(newCart);
    }

    /**
     * Decreases quantity of product in cart, or removes it. Then search and refresh
     * promotions
     * applicable to cart.
     */
    public Cart removeProduct(Cart cart, Product product, int quantity) throws Exception {
        log.info("--- removeProduct --- Removing " + quantity + " of product " + product + " from " + cart);

        Optional<CartItem> optionalCartItem = cart.contains(product);

        if (!optionalCartItem.isPresent()) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND_IN_CART);
        }

        if (quantity < 1) {
            throw new NegativeRemoveQuantityException(CANNOT_REMOVE_NEGATIVE_AMOUNT);
        }

        CartItem cartItem = optionalCartItem.get();

        if (cartItem.getQuantity() - quantity <= 0) {
            deleteCartItem(cart, cartItem);
        } else {
            updateExistingCartItemQuantity(cartItem, cartItem.getQuantity() - quantity);
        }

        Cart newCart = findCartById(cart.getId());
        return updateWithPromotions(newCart);
    }

    /**
     * Add new product to cart
     */
    private void addNewCartItemWithQuantity(Cart cart, Product product, int quantity) throws Exception {
        if (validate(cart, product, quantity)) {
            CartItem cartItem = new CartItem(cart, product, quantity);
            log.info("Saving cartItem after adding new product: " + cartItem);

            CartItem savedCartItem = cartItemRepository.save(cartItem);
            log.info("Saved cartItem: " + savedCartItem);
            log.info("Cart after saving cartItem: " + cartRepository.findById(cart.getId()));
        } else {
            throw new QuantityUnavailableException(QUANTITY_UNAVAILABLE);
        }
    }

    /**
     * Update cart item with provided quantity
     */
    private void updateExistingCartItemQuantity(CartItem cartItem, int quantity) {
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    /**
     * Deletes a cart item
     */
    private void deleteCartItem(Cart cart, CartItem cartItem) {
        cartItemRepository.delete(cartItem);
        cart.getCartItems().remove(cartItem);
        Cart newCart = cartRepository.save(cart);
        log.info("After deleting cartItem " + cartItem + " from cart: " + newCart);
    }

    /**
     * Searches for and adds any new promotions to the cart
     */
    private Cart updateWithPromotions(Cart cart) {
        List<Promotion> promotions = promotionService.findPromotionsByCart(cart);
        log.info("--- updateWithPromotions --- Promotions applicable: " + promotions);

        cart.setPromotions(promotions);
        cartRepository.save(cart);
        log.info("Saved cart after updating promotions: " + cart);

        return cartRepository.findById(cart.getId()).get();
    }

    /**
     * Validates whether it is okay to add the requested quantity of product to
     * the cart.
     */
    private boolean validate(Cart cart, Product product, int quantity) {
        List<CartItem> cartItems = cart.getCartItems();

        // Quantity requested to be added to cart exceeds product availability
        if (quantity > product.getQuantity()) {
            return false;
        }

        // Checks if product already exists in the cart
        Optional<CartItem> optionalCartItem = cartItems.stream().filter(item -> item.getProduct().equals(product))
                .findFirst();
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();

            // Ensures that the resulting total quantity does not exceed product
            // availability
            if (cartItem.getQuantity() + quantity > product.getQuantity()) {
                return false;
            }
        }

        // Check not already in cart
        if (cartItems.isEmpty() || !cartItems.contains(product)) {
            return true;
        } else {
            return false;
        }
    }
}
