package com.example.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST})
    @JoinColumn(name="cart_id")
    private List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    /**
     * Adds a product to the cart with the specified quantity,
     * if available.
     *
     * @param product
     * @param quantity
     */
    public void addProduct(Product product, int quantity) {
        if (cartItems.isEmpty() || (!cartItems.contains(product) && quantity <= product.getQuantity())) {
            cartItems.add(new CartItem(product, quantity));
        }
    }

    /**
     * Returns the number of items in the cart
     *
     * @return
     */
    public int totalItems() {
        return cartItems.size();
    }
}
