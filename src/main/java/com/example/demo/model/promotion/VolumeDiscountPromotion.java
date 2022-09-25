package com.example.demo.model.promotion;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;

import javax.persistence.Entity;

/**
 * Conditional volume discount promotion:
 *
 * Purchasing more than {@link #thresholdQuantity} will
 * net a {@link #percentDiscount} on entire cartItem.
 */
@Entity
public class VolumeDiscountPromotion extends Promotion {
    private int thresholdQuantity;
    private double percentDiscount;

    public VolumeDiscountPromotion() {}

    public VolumeDiscountPromotion(Product product, int thresholdQuantity, double percentDiscount) {
        super(product);
        this.thresholdQuantity = thresholdQuantity;
        this.percentDiscount = percentDiscount;
    }

    public int getThresholdQuantity() {
        return thresholdQuantity;
    }

    public double getPercentDiscount() {
        return percentDiscount;
    }

    @Override
    public Cart applyPromotion(Cart cart) {
        return null;
    }

    @Override
    public String toString() {
        return "VolumeDiscountPromotion{" +
                "thresholdQuantity=" + thresholdQuantity +
                ", percentDiscount=" + percentDiscount +
                ", product=" + product +
                '}';
    }
}
