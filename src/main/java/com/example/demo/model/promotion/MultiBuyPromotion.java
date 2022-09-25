package com.example.demo.model.promotion;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Multi-buy promotion:
 *
 * Purchasing {@link #requiredQuantity} of {@link Promotion#product}
 * nets {@link #promotionQuantity} of the product
 */
@Entity
public class MultiBuyPromotion extends Promotion {
    private int requiredQuantity;
    private int promotionQuantity;

    public MultiBuyPromotion() {}

    public MultiBuyPromotion(Product product, int requiredQuantity, int promotionQuantity) {
        super(product);
        this.requiredQuantity = requiredQuantity;
        this.promotionQuantity = promotionQuantity;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }

    @Override
    public double applyPromotion(Cart cart) {
        return 0.0;
    }

    @Override
    public String toString() {
        return "MultiBuyPromotion{" +
                "requiredQuantity=" + requiredQuantity +
                ", promotionQuantity=" + promotionQuantity +
                ", product=" + product +
                '}';
    }
}
