package com.example.demo.model.checkout;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.cart.CartItem;

import javax.persistence.*;
import java.text.DecimalFormat;

@Entity
public class CheckoutSession {

    private static final DecimalFormat df = new DecimalFormat("0.00");

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

    public String getTotal() {
        return df.format(cart.calculateNetTotal());
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
