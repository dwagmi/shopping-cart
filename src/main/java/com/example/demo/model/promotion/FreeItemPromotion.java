package com.example.demo.model.promotion;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger log = LoggerFactory.getLogger(FreeItemPromotion.class);

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
    public Cart applyPromotion(Cart cart) {
        log.info("apply");
        return null;
    }

    @Override
    public String toString() {
        return "FreeItemPromotion{" +
                "promotionProduct=" + promotionProduct +
                ", product=" + product +
                '}';
    }
}
