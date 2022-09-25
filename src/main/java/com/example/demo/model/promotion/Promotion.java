package com.example.demo.model.promotion;

import com.example.demo.model.product.Product;

/**
 * A promotion applies to a specific product SKU.
 */
public abstract class Promotion {

    /**
     * The product to which the promotion applies
     */
    public Product product;



    public abstract String promotionType();
}
