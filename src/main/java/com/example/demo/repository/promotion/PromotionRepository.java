package com.example.demo.repository.promotion;

import com.example.demo.model.checkout.CheckoutSession;
import com.example.demo.model.promotion.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByProductId(Long productId);
}
