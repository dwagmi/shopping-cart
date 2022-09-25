package com.example.demo.api;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.promotion.Promotion;
import com.example.demo.service.promotion.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/promotion")
public class PromotionApi {
    private static Logger log = LoggerFactory.getLogger(PromotionApi.class);

    private final PromotionService promotionService;

    @Autowired
    public PromotionApi(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping(path="/all")
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        List<Promotion> allPromotions = promotionService.findAllPromotions();
        log.info("Retrieved all promotions: " + allPromotions);
        return ResponseEntity.status(HttpStatus.OK).body(allPromotions);
    }
}
