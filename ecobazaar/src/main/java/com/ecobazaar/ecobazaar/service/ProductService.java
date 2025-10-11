package com.ecobazaar.ecobazaar.service;

import org.springframework.stereotype.Service;
import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // CREATE - Add new product
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // READ - Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // READ - Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // UPDATE - Update existing product
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDetails(updatedProduct.getDetails());
                    product.setPrice(updatedProduct.getPrice());
                    product.setCarbonImpact(updatedProduct.getCarbonImpact());
                    product.setEcoCertified(updatedProduct.getEcoCertified());
                    product.setSellerId(updatedProduct.getSellerId());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // DELETE - Delete product by ID
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }
}