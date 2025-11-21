package com.ecobazaar.ecobazaar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecobazaar.ecobazaar.dto.CartSummaryDTO;
import com.ecobazaar.ecobazaar.model.CartItem;
import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.repository.CartRepository;
import com.ecobazaar.ecobazaar.repository.ProductRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartItem addToCart(CartItem cartItem) {
        return cartRepository.save(cartItem);
    }

    public CartSummaryDTO getCartSummary(Long userId) {

        List<CartItem> cartItems = cartRepository.findByUserId(userId);

        double totalPrice = 0;
        double totalCarbonUsed = 0;
        double totalCarbonSaved = 0;
        String ecoSuggestion = null;

        for (CartItem item : cartItems) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            double carbon = product.getCarbonImpact() * item.getQuantity();
            totalCarbonUsed += carbon;
            totalPrice += product.getPrice() * item.getQuantity();

            // ONLY for non-eco products → find eco alternative
            if (!Boolean.TRUE.equals(product.getEcoCertified())) {

                String[] words = product.getName().split(" ");
                String keyword = words[words.length - 1].replaceAll("[^a-zA-Z]", "");

                Optional<Product> ecoAlt = productRepository
                        .findFirstByEcoCertifiedTrueAndNameContainingIgnoreCase(keyword);

                if (ecoAlt.isPresent()) {

                    double ecoCarbon = ecoAlt.get().getCarbonImpact();
                    double saved = (product.getCarbonImpact() - ecoCarbon) * item.getQuantity();

                    if (saved > 0) {
                        totalCarbonSaved += saved;

                        // only show suggestion once
                        if (ecoSuggestion == null) {
                            ecoSuggestion = "💡 Switch to " + ecoAlt.get().getName()
                                    + " and save " + String.format("%.2f", saved) + " kg CO₂!";
                        }
                    }
                }
            }
        }

        return new CartSummaryDTO(
                cartItems,
                totalPrice,
                totalCarbonUsed,
                totalCarbonSaved,
                ecoSuggestion
        );
    }

    public void removeFromCart(Long id) {
        cartRepository.deleteById(id);
    }
}