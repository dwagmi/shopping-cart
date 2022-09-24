package com.example.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST})
    @JoinColumn(name="cart_id")
    private List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getCartItems() {
        return cartItems;
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
