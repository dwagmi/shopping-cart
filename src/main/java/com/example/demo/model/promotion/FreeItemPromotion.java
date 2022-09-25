package com.example.demo.model.promotion;

import com.example.demo.model.product.Product;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Free Item promotion:
 *
 * Purchasing one of {@link Promotion#product} nets a free
 * {@link #promotionProduct}
 */
@Entity
public class FreeItemPromotion extends Promotion {
    @ManyToOne
    @JoinColumn(name = "promotion_product_id")
    private Product promotionProduct;

    public FreeItemPromotion() {}

    public FreeItemPromotion(Product product, Product promotionProduct) {
        super(product);
        this.promotionProduct = promotionProduct;
    }

    public Product getPromotionProduct() {
        return promotionProduct;
    }

    @Override
    public String toString() {
        return "FreeItemPromotion{" +
                "promotionProduct=" + promotionProduct +
                ", product=" + product +
                '}';
    }
}
