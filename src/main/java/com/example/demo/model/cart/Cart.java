package com.example.demo.model.cart;

import com.example.demo.model.checkout.CheckoutSession;
import com.example.demo.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name="cart_id")
    private List<CartItem> cartItems = new ArrayList<>();

//    @OneToOne
//    @JoinColumn(name="cart_id")
//    private CheckoutSession checkoutSession;

    public Long getId() {
        return id;
    }


    /**
     * Returns the items in the cart, sorted by cartItem id
     *
     * @return
     */
    public List<CartItem> getCartItems() {
        cartItems.sort(Comparator.comparing(CartItem::getId));
        return cartItems;
    }

    /**
     * Returns the number of items in the cart
     *
     * @return
     */
    public int totalItems() {
        return cartItems.size();
    }

    /**
     * Extracts the list of products from cart items.
     *
     * @return
     */
    @JsonIgnore
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        for (CartItem cartItem: cartItems) {
            products.add(cartItem.getProduct());
        }
        return products;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", cartItems=" + cartItems +
                '}';
    }
}
