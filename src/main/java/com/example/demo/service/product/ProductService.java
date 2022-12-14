package com.example.demo.service.product;

import com.example.demo.model.product.Product;
import com.example.demo.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllProducts() {
        return productRepository.findAllByOrderByIdAsc();
    }

    public Optional<Product> findProductById(Long productId) {
        return productRepository.findById(productId);
    }
}
