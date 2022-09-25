package com.example.demo.repository.checkout;

import com.example.demo.model.checkout.CheckoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckoutSessionRepository extends JpaRepository<CheckoutSession, Long> {
    Optional<CheckoutSession> findByCartId(Long cartId);
}
