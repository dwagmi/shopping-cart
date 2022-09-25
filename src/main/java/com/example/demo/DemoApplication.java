package com.example.demo;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;
import com.example.demo.model.promotion.Promotion;
import com.example.demo.model.promotion.VolumeDiscountPromotion;
import com.example.demo.repository.cart.CartItemRepository;
import com.example.demo.repository.product.ProductRepository;
import com.example.demo.service.cart.CartService;
import com.example.demo.service.promotion.PromotionService;
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
	public CommandLineRunner demo(ProductRepository productRepository,
								  CartService cartService,
								  CartItemRepository cartItemRepository,
								  PromotionService promotionService
	) {
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

			// Add promotions
			Promotion promotion = new VolumeDiscountPromotion(toAdd, 3, 0.10);
			log.info("Adding promotion: " + promotion);
			promotionService.addPromotion(promotion);

			for (Promotion p: promotionService.findAllPromotions()) {
				System.out.println(p);
			}
		};
	}

}
