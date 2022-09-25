package com.example.demo;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.product.Product;
import com.example.demo.model.promotion.FreeItemPromotion;
import com.example.demo.model.promotion.BuyXGetYPromotion;
import com.example.demo.model.promotion.VolumeDiscountPromotion;
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

	private static Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(ProductRepository productRepository,
								  CartService cartService,
								  PromotionService promotionService
	) {
		return (args) -> {
			log.info("----- Start seed data -----");
			// Populate a sample cart
			Cart cart = cartService.createCart();

			// Retrieve seeded products
			Product googleHome = productRepository.findById(1L).get();
			Product macbook = productRepository.findById(2L).get();
			Product alexa = productRepository.findById(3L).get();
			Product pi = productRepository.findById(4L).get();

			cartService.addProduct(cart, macbook, 3);

			/**
			 * While Products are seeded using `import.sql`, Promotions are seeded
			 * directly at app level.
			 */
			// Buying more than 3 Alexa Speakers will have a 10% discount on all Alexa speakers
			promotionService.addPromotion(new VolumeDiscountPromotion(alexa, 3, 0.10));
			// Each sale of a MacBook Pro comes with a free Raspberry Pi B
			promotionService.addPromotion(new FreeItemPromotion(macbook, pi));
			// Buy 3 Google Homes for the price of 2
			promotionService.addPromotion(new BuyXGetYPromotion(googleHome, 2, 3));

			log.info("----- End seed data -----");
		};
	}

}
