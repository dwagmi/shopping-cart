package com.example.demo.service.checkout;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.checkout.CheckoutSession;

public interface BaseCheckoutService {
    CheckoutSession checkout(Cart cart);
}
