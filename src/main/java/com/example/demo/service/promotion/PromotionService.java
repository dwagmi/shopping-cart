package com.example.demo.service.promotion;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;
import com.example.demo.model.promotion.Promotion;
import com.example.demo.repository.promotion.PromotionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionService {
    private static Logger log = LoggerFactory.getLogger(PromotionService.class);

    private final PromotionRepository promotionRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> findAllPromotions() {
        return promotionRepository.findAll();
    }

    public List<Promotion> findPromotionsByProductId(Long productId) {
        List<Promotion> promotions = promotionRepository.findByProductId(productId);
        log.info("found promotions for productId " + productId + ": " + promotions);
        return promotions;
    }

    /**
     * Searches for promotions for each product in the cart and returns all
     * applicable promotions in a single list
     *
     * @param cart
     * @return
     */
    public List<Promotion> findPromotionsByCart(Cart cart) {
        List<Promotion> promotions = new ArrayList<>();
        log.info("--- findPromotionsByCart --- for cart: " + cart);

        List<Product> products = cart.getProducts();
        log.info("Products in cart: " + products);

        products.forEach(product -> promotions.addAll(findPromotionsByProductId(product.getId())));
        return promotions;
    }

    public Promotion addPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }
}
