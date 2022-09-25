package com.example.demo.repository.checkout;

import com.example.demo.model.checkout.CheckoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckoutSessionRepository extends JpaRepository<CheckoutSession, Long> {
}
