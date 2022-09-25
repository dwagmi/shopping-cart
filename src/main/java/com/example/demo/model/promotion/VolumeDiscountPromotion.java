package com.example.demo.model.promotion;

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
}
