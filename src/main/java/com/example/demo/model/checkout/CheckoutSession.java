package com.example.demo.model.checkout;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.cart.CartItem;
import com.example.demo.model.promotion.Promotion;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.List;

@Entity
public class CheckoutSession {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    /**
     * A checkoutSession can include multiple promotions, and a promotion
     * can be applied to multiple checkoutSessions, so a mapping table is used to
     * model the many-to-many relationship.
     */
    @ManyToMany
    @JoinTable(
        name = "checkout_session_promotion",
        inverseJoinColumns = { @JoinColumn(name = "promotion_id") }
    )
    private List<Promotion> promotions;

    public CheckoutSession() {}

    public CheckoutSession(Cart cart) {
        this.cart = cart;
    }

    /**
     * Calculates the total cost of items in the cart after discount
     *
     * @param cart
     * @return
     */
    private double calculateNetTotal(Cart cart) {
        double netTotal = 0;
        netTotal += calculateGrossTotal(cart);

        // TODO: Find promotions and apply them to the checkout.
        return netTotal;
    }


    private double calculateGrossTotal(Cart cart) {
        double grossTotal = 0;
        for (CartItem cartItem: cart.getCartItems()) {
            grossTotal += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return grossTotal;
    }

    public Cart getCart() {
        return cart;
    }

    public String getTotal() {
        return df.format(calculateNetTotal(cart));
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
