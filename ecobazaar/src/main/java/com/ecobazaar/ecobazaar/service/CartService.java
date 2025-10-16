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
	    double totalCarbon = 0;
	    String ecoSuggestion = null;

	    for (CartItem item : cartItems) {
	        Product product = productRepository.findById(item.getProductId())
	                .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

	        totalPrice += product.getPrice() * item.getQuantity();
	        totalCarbon += product.getCarbonImpact() * item.getQuantity();

	        if (Boolean.FALSE.equals(product.getEcoCertified())) {
	            Optional<Product> ecoAlt = productRepository
	                    .findFirstByEcoCertifiedTrueAndNameContainingIgnoreCase(product.getName());

	            if (ecoAlt.isPresent()) {
	                double saved = product.getCarbonImpact() - ecoAlt.get().getCarbonImpact();

	                if (saved > 0.5) {
	                    ecoSuggestion = "💡 Switch to " + ecoAlt.get().getName()
	                            + " and save " + saved + " kg CO₂!";
	                }
	            }
	        }
	    }

	    return new CartSummaryDTO(cartItems, totalPrice, totalCarbon, ecoSuggestion);
	}

	
	public void removeFromCart(Long id) {
		cartRepository.deleteById(id);
	}

}