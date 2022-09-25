package com.example.demo.model.promotion;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.cart.CartItem;
import com.example.demo.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.util.Optional;

/**
 * A promotion applies to a specific product SKU.
 */
@Entity
@JsonDeserialize(as = FreeItemPromotion.class)
@DiscriminatorColumn(name = "promotion_type")
public abstract class Promotion {

    @Id
    @JsonIgnore
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

    public Promotion() {
        super();
    }

    public Promotion(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getPromotionType() {
        return promotionType;
    }

    @JsonIgnore
    public Product getProduct() {
        return product;
    }

    public String getProductOnPromotion() {
        return product.getName();
    }

    /**
     * Each child promotion updates the cart according to their
     * specific promotion details. Returns the dollar amount saved
     * after applying the promotion.
     *
     * @param cart
     * @return
     */
    public abstract double applyPromotion(Cart cart);

    /**
     * Concrete method in abstract base class to find qty of given product
     */
    protected int findQtyOfProductInCart(Cart cart, Product p) {
        Optional<CartItem> optionalCartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().equals(p))
                .findFirst();
        if (!optionalCartItem.isPresent()) {
            return 0;
        }
        return optionalCartItem.get().getQuantity();
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", product=" + product +
                '}';
    }
}
