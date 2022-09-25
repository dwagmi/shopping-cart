package com.example.demo.model.promotion;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;

import javax.persistence.*;

/**
 * A promotion applies to a specific product SKU.
 */
@Entity
@DiscriminatorColumn(name = "promotion_type")
public abstract class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The product to which the promotion applies
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;

    @Column(name = "promotion_type", insertable = false, updatable = false)
    private String promotionType;

    public Promotion() {}

    public Promotion(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public Product getProduct() {
        return product;
    }

    /**
     * Each child promotion updates the cart according to their
     * specific promotion details
     *
     * @param cart
     * @return
     */
    public abstract Cart applyPromotion(Cart cart);

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", product=" + product +
                '}';
    }
}
