package com.example.demo.service.cart;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.cart.CartItem;
import com.example.demo.model.product.Product;
import com.example.demo.model.promotion.Promotion;
import com.example.demo.repository.cart.CartItemRepository;
import com.example.demo.repository.cart.CartRepository;
import com.example.demo.service.promotion.PromotionService;
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
    private static Logger log = LoggerFactory.getLogger(CartService.class);

    private final PromotionService promotionService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(PromotionService promotionService, CartRepository cartRepository, CartItemRepository cartItemRepository) {
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
    public Optional<Cart> findCartById(Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            return Optional.of(updateWithPromotions(optionalCart.get()));
        }
        return null;
    }

    /**
     * Creates a new cart and returns the new empty cart.
     */
    public Cart createCart() {
        return cartRepository.save(new Cart());
    }

    /**
     * Adds product to cart or update if already in cart. Then search and update cart with
     * any applicable promotions.
     */
    public Cart addProduct(Cart cart, Product product, int quantity) {
        log.info("Adding " + quantity + " of product " + product + " to " + cart);

        Optional<CartItem> optionalCartItem = cart.getCartItems().stream().filter(item -> item.getProduct().equals(product)).findFirst();
        if (optionalCartItem.isPresent()) {
            updateExistingCartItemQuantity(cart, optionalCartItem.get(), quantity);
        } else {
            addNewCartItemWithQuantity(cart, product, quantity);
        }

        return updateWithPromotions(cart);
    }

    /**
     * Add new product to cart
     */
    private void addNewCartItemWithQuantity(Cart cart, Product product, int quantity) {
        if (validate(cart, product, quantity)) {
            cartItemRepository.save(new CartItem(cart, product, quantity));
        }
    }

    /**
     * Update cart item with provided quantity
     */
    private void updateExistingCartItemQuantity(Cart cart, CartItem cartItem, int quantity) {
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    /**
     * Searches for and adds any new promotions to the cart
     */
    private Cart updateWithPromotions(Cart cart) {
        List<Promotion> promotions = promotionService.findPromotionsByCart(cart);
        log.info("Promotions applicable: " + promotions);
        cart.setPromotions(promotions);
        return cartRepository.save(cart);
    }

    /**
     * Validates whether it is okay to add the requested quantity of product to
     * the cart.
     */
    private boolean validate(Cart cart, Product product, int quantity) {
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
}
