package com.example.demo;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(ProductRepository productRepository, CartService cartService, CartItemRepository cartItemRepository) {
		return (args) -> {
			// Populate with fake data
			Cart cart = cartService.createCart();
			log.info("Retrieved cart " + cart);
			Product toAdd = productRepository.findById(1L).get();
			log.info("Product to add: " + toAdd);
//			cartService.addProduct(cart, new Product("sku-001", "Computer", 39.99, 5), 3);
			cartService.addProduct(cart, toAdd, 3);

			// Get all products
			for (Product product: productRepository.findAll()) {
				System.out.println(product);
			}

			// Get all carts
//			for (Cart c: cartService.getAllCarts()) {
//				System.out.println(c);
//			}

			// Get all cartItems
//			for (CartItem c: cartItemRepository.findAll()) {
//				System.out.println(c);
//			}
		};
	}

}
