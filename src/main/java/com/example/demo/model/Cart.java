package com.example.demo.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cart {

    @Id
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST})
    @JoinColumn(name="cart_id")
    private List<CartItem> cartItems;
}
