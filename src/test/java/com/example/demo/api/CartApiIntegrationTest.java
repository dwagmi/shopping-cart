package com.example.demo.api;

import com.example.demo.model.cart.Cart;
import com.example.demo.model.cart.CartItem;
import com.example.demo.model.product.Product;
import com.example.demo.repository.product.ProductRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartApiIntegrationTest {
    private static Logger log = LoggerFactory.getLogger(CartApiIntegrationTest.class);

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void givenCartApi_whenGetCarts_thenShouldReturnAListOfAllCarts() {
        ResponseEntity<List<Cart>> response = restTemplate.exchange(
                baseUrl + port + "/carts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        List<Cart> result = response.getBody();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(result.get(0) instanceof Cart);
    }

    @Test
    public void givenCartApi_whenPostNewCart_thenShouldReturnEmptyCart() {
        ResponseEntity<Cart> response = restTemplate.exchange(
                baseUrl + port + "/cart/new",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        Cart cart = response.getBody();

        assertTrue(cart instanceof Cart);
        assertTrue(cart.getCartItems().size() == 0);
    }

    @Test
    @Disabled
    public void givenCartApi_whenPostAddToCart_thenShouldReturnUpdatedCart() {
        Long cartId = 1L;
//        Product product = productRepository.save(new Product("sku-001", "Test product", 39.99, 5));
        Product product = productRepository.findById(3L).get();
        int quantity = 1;

        log.info("Products before test request: " + productRepository.findAll());

        ResponseEntity<Cart> response = restTemplate.exchange(
                baseUrl + port + "/cart/" + cartId + "/" + product.getId() + "/" + quantity,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        Cart cart = response.getBody();

        log.info(cart.toString());

        Optional<CartItem> optionalCartItem = cart.getCartItems().stream().filter(item -> item.getProduct().getSku().equals(product.getSku())).findFirst();
        boolean productExistsInCart = optionalCartItem.isPresent();
        assertTrue(cart instanceof Cart);
        assertTrue(productExistsInCart);
        assertEquals(optionalCartItem.get().getQuantity(), quantity);
    }

//    @Test
//    public void givenCartApi_whenCartEligibleForBuyXGetXPromotion_thenShouldApplyPromotion() {
//        ResponseEntity<Cart> response = restTemplate.exchange(
//                getCartsUrl(),
//                HttpMethod.POST,
//                null,
//                new ParameterizedTypeReference<List<Cart>>() {}
//        );
//    }


}
