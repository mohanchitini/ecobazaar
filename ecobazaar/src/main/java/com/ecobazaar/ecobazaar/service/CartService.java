package com.ecobazaar.ecobazaar.service;

import com.ecobazaar.ecobazaar.model.CartItem;
import com.ecobazaar.ecobazaar.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    public CartItem addToCart(CartItem cartItem) {
        return cartRepository.save(cartItem);
    }

    public List<CartItem> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public void removeFromCart(Long id) {
        cartRepository.deleteById(id);
    }
}
