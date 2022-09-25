package com.example.demo.service.checkout;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.checkout.CheckoutSession;
import com.example.demo.repository.checkout.CheckoutSessionRepository;
import com.example.demo.service.promotion.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckoutService implements BaseCheckoutService {
    private static Logger log = LoggerFactory.getLogger(CheckoutService.class);

    private final CheckoutSessionRepository checkoutSessionRepository;
    private final PromotionService promotionService;

    @Autowired
    public CheckoutService(CheckoutSessionRepository checkoutSessionRepository, PromotionService promotionService) {
        this.checkoutSessionRepository = checkoutSessionRepository;
        this.promotionService = promotionService;
    }

    /**
     * Return a checkoutSession to be passed to a payment gateway to convert into an Order.
     *
     * @param cart
     * @return
     */
    public CheckoutSession checkout(Cart cart) {
        log.info("checking out " + cart);

        // Find and apply promotions to the cart


        CheckoutSession checkoutSession;
        Optional<CheckoutSession> optionalCheckoutSession = checkoutSessionRepository.findByCartId(cart.getId());

        if (optionalCheckoutSession.isPresent()) {
            // Retrieves checkoutSession for the cart, if it exists
            log.info("found checkoutSession for cart " + cart.getId() + ": " + optionalCheckoutSession.get());

            checkoutSession = optionalCheckoutSession.get();
            checkoutSession.setCart(cart);
        } else {
            // Creates a new checkout session
            checkoutSession = checkoutSessionRepository.save(new CheckoutSession(cart));
        }

        return checkoutSession;
    }
}
