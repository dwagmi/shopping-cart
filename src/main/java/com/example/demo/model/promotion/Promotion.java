package com.example.demo.model.promotion;

import com.example.demo.model.product.Product;

import javax.persistence.*;

/**
 * A promotion applies to a specific product SKU.
 */
@Entity
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The product to which the promotion applies
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;

}
