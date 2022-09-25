package com.example.demo.model.promotion;

import com.example.demo.model.cart.Cart;
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

    public FreeItemPromotion() {
    }

    public FreeItemPromotion(Product product, Product promotionProduct) {
        super(product);
        this.promotionProduct = promotionProduct;
    }

    public String getFreeProduct() {
        return promotionProduct.getName();
    }

    /**
     * If the required product is in the cart, then the cost of the promotional
     * product
     * is deducted, if it exists in the cart.
     *
     * If there is >1 required product in the cart, then the cost of each
     * promotional product
     * that exists in the cart is deducted, up to the quantity of the required
     * product.
     *
     * @return total amount saved by applying this promotion
     */
    @Override
    public double applyPromotion(Cart cart) {
        int qtyOfProductInCart = findQtyOfProductInCart(cart, product);
        int qtyOfPromoProductInCart = findQtyOfProductInCart(cart, promotionProduct);
        return promotionProduct.getPrice() * Math.min(qtyOfProductInCart, qtyOfPromoProductInCart);
    }

    @Override
    public String toString() {
        return "FreeItemPromotion{" +
                "promotionProduct=" + promotionProduct +
                ", product=" + product +
                '}';
    }
}
