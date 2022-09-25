package com.example.demo.service.checkout;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.checkout.CheckoutSession;
import com.example.demo.repository.checkout.CheckoutSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckoutService implements BaseCheckoutService {
    Logger log = LoggerFactory.getLogger(CheckoutService.class);

    private final CheckoutSessionRepository checkoutSessionRepository;

    @Autowired
    public CheckoutService(CheckoutSessionRepository checkoutSessionRepository) {
        this.checkoutSessionRepository = checkoutSessionRepository;
    }

    public CheckoutSession checkout(Cart cart) {
        log.info("checking out " + cart);

        Optional<CheckoutSession> optionalCheckoutSession = checkoutSessionRepository.findByCartId(cart.getId());

        if (optionalCheckoutSession.isPresent()) {
            // Retrieves checkoutSession for the cart, if it exists
            log.info("found checkoutSession for cart " + cart.getId() + ": " + optionalCheckoutSession.get());

            CheckoutSession checkoutSession = optionalCheckoutSession.get();
            checkoutSession.setCart(cart);
            return checkoutSession;
        } else {
            // Creates a new checkout session
            CheckoutSession checkoutSession = checkoutSessionRepository.save(new CheckoutSession(cart));
            return checkoutSession;
        }
    }
}
