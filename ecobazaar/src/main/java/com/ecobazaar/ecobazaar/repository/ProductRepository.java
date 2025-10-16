package com.ecobazaar.ecobazaar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecobazaar.ecobazaar.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByEcoCertifiedTrue();
	
    List<Product> findByEcoCertifiedTrueOrderByCarbonImpactAsc();
    
    Optional<Product> findFirstByEcoCertifiedTrueAndNameContainingIgnoreCase(String namePart);


}