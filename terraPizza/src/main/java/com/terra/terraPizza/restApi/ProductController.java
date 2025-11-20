package com.terra.terraPizza.restApi;

import com.terra.terraPizza.DataAcces.ProductRepository;
import com.terra.terraPizza.Entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public List<Product> getByCategory(@RequestParam String category) {
        return productRepository.findByCategory(category);
    }
}
