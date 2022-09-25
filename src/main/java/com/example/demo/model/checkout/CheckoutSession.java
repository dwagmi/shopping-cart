package com.example.demo.model.checkout;

import com.example.demo.model.cart.Cart;

import javax.persistence.*;

@Entity
public class CheckoutSession {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    public CheckoutSession() {}

    public CheckoutSession(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
