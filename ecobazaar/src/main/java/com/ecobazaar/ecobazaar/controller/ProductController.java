package com.ecobazaar.ecobazaar.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.model.User;
import com.ecobazaar.ecobazaar.repository.UserRepository;
import com.ecobazaar.ecobazaar.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;

    public ProductController(ProductService productService, UserRepository userRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User seller = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Seller not found"));
        product.setSellerId(seller.getId());
        return productService.createProduct(product);
    }

    @GetMapping
    public List<Product> listAllProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PutMapping("/{id}")
    public Product updateProductDetails(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProductDetails(id, product);
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProductDetails(@PathVariable Long id) {
        productService.deleteProductDetails(id);
    }

    @GetMapping("/eco")
    public List<Product> getEcoCertified() {
        return productService.getEcoCertifiedProducts();
    }

    @GetMapping("/eco/sorted")
    public List<Product> getEcoCertifiedSorted() {
        return productService.getEcoCertifiedSortedByCarbonImpact();
    }
}