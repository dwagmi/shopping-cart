package com.example.demo.model.promotion;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;

import javax.persistence.Entity;

/**
 * Multi-buy promotion:
 *
 * Purchasing {@link #buyQuantity} of {@link Promotion#product}
 * nets {@link #getQuantity} of the product
 */
@Entity
public class BuyXGetYPromotion extends Promotion {
    private int buyQuantity;
    private int getQuantity;

    public BuyXGetYPromotion() {}

    public BuyXGetYPromotion(Product product, int buyQuantity, int getQuantity) {
        super(product);
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGetQuantity() {
        return getQuantity;
    }

    @Override
    public double applyPromotion(Cart cart) {
        int qtyFreePerBatch = getQuantity - buyQuantity;
        int qtyInCart = findQtyOfProductInCart(cart, product);
        int batches = qtyInCart / getQuantity;
        return batches * qtyFreePerBatch * product.getPrice();
    }

    @Override
    public String toString() {
        return "MultiBuyPromotion{" +
                "buyQuantity=" + buyQuantity +
                ", getQuantity=" + getQuantity +
                ", product=" + product +
                '}';
    }
}
