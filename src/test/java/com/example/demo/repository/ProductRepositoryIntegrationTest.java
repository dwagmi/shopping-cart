package com.example.demo.repository;

import com.example.demo.model.product.Product;
import com.example.demo.repository.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration testing for the ProductRepository
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRepositoryIntegrationTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    public void givenProductRepository_whenSaveAndRetrieveProduct_thenOK() {
        Product product = productRepository.save(new Product("sku-001", "Test product", 39.99, 5));
        Optional<Product> foundProduct = productRepository.findById(product.getId());

        assertTrue(foundProduct.isPresent());
        assertEquals(product.getName(), foundProduct.get().getName());
    }
}
