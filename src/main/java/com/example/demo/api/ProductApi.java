package com.example.demo.api;

import com.example.demo.model.product.Product;
import com.example.demo.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductApi {
    Logger log = LoggerFactory.getLogger(ProductApi.class);

    private final ProductService productService;

    @Autowired
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(method = RequestMethod.GET, value="/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> allProducts = productService.findAllProducts();
        log.info("Retrieved all products: " + allProducts);
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }
}
