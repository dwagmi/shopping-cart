package com.example.demo.model.cart;

import com.example.demo.model.product.Product;
import com.example.demo.model.promotion.Promotion;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Entity
public class Cart {

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
     * A Cart can include multiple promotions, and a promotion
     * can be applied to multiple Carts - a mapping table is used to
     * model the many-to-many relationship.
     *
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

    public Set<Promotion> getPromotions() {
        return promotions;
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
     * Extracts the list of products from cart items.
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
    public double calculateNetTotal() {
        double netTotal = 0;
        netTotal += calculateGrossTotal();

        // Mutates the given cart with the promotion.
        for (Promotion promotion: promotions) {
            promotion.applyPromotion(this);
        }
        return netTotal;
    }

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
