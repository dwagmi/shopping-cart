package com.example.demo.model.cart;

import com.example.demo.model.product.Product;
import com.example.demo.model.promotion.Promotion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Domain model of the shopping cart.
 *
 * Holds cart items (product + quantity), applicable promotions, as well as calculates
 * the gross, net and savings amounts of the cart.
 *
 * {@link JsonPropertyOrder} is used to specify the order of fields in the API response.
 */
@Entity
@JsonPropertyOrder({ "id", "cartItems", "promotions", "grossTotal", "savings", "netTotal" })
public class Cart {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Cart items belonging to a cart are almost always required when a Cart
     * look-up is made, so cart items are fetched eagerly.
     */
    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name="cart_id")
    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * A join table is used to model the many-to-many relationship between carts and promotions
     * Similar to cart items, a cart's promotions are also fetched eagerly.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cart_promotion",
            inverseJoinColumns = { @JoinColumn(name = "promotion_id") }
    )
    private Set<Promotion> promotions;

    public Long getId() {
        return id;
    }

    /**
     * Returns the set of promotions applicable to the current cart.
     *
     * Whenever the cart is updated,
     * {@link com.example.demo.service.cart.CartService} searches for applicable
     * promotions through {@link com.example.demo.service.promotion.PromotionService}
     * and adds them to this set.
     */
    public Set<Promotion> getPromotions() {
        return promotions;
    }

    /**
     * Converts and saves the provided list of promotions to set.
     */
    public void setPromotions(List<Promotion> promotions) {
        this.promotions =  new HashSet<Promotion>(promotions);
    }

    /**
     * Calculates and returns the cart's gross total before promotions are
     * applied, as a formatted string to 2dp.
     * Net, gross and savings value are functional instead of instance variables
     * to ensure stale state is not retained and returned by accident.
     */
    public String getGrossTotal() {
        return df.format(calculateGrossTotal());
    }

    /**
     * Calculates and returns amount saved from promotions
     */
    public String getSavings() {
        return df.format(calculateGrossTotal() - calculateNetTotal());
    }

    /**
     * Calculates and returns the cart's net total after promotions are applied,
     * as a formatted string to 2 decimal places
     */
    public String getNetTotal() {
        return df.format(calculateNetTotal());
    }

    /**
     * Returns the items in the cart, sorted by cartItem id
     */
    public List<CartItem> getCartItems() {
        cartItems.sort(Comparator.comparing(CartItem::getId));
        return cartItems;
    }

    /**
     * Returns the number of items in the cart
     */
    public int totalItems() {
        return cartItems.size();
    }

    /**
     * Extracts the list of products from cart items
     */
    @JsonIgnore
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        for (CartItem cartItem: cartItems) {
            products.add(cartItem.getProduct());
        }
        return products;
    }

    /**
     * Calculates the total cost of items in the cart after discount
     */
    private double calculateNetTotal() {
        double netTotal = 0;
        netTotal += calculateGrossTotal();

        // Mutates the given cart with the promotion.
        for (Promotion promotion: promotions) {
            promotion.applyPromotion(this);
        }
        return netTotal;
    }

    /**
     * Calculates the total cost of items before discount
     */
    private double calculateGrossTotal() {
        double grossTotal = 0;
        for (CartItem cartItem: getCartItems()) {
            grossTotal += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return grossTotal;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", cartItems=" + cartItems +
                '}';
    }
}
