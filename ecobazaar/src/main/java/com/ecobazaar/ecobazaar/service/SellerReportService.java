package com.ecobazaar.ecobazaar.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecobazaar.ecobazaar.dto.SellerReport;
import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.model.User;
import com.ecobazaar.ecobazaar.repository.OrderItemRepository;
import com.ecobazaar.ecobazaar.repository.ProductRepository;
import com.ecobazaar.ecobazaar.repository.UserRepository;

@Service
public class SellerReportService {

	private final OrderItemRepository orderItemRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	
	public SellerReportService(OrderItemRepository orderItemRepository, UserRepository userRepository, ProductRepository productRepository) {
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}
	
	
	public SellerReport getSellerReport(String email) {
		User seller = userRepository.findByEmail(email)
				.orElseThrow(()-> new RuntimeException("Seller Not Found"));
		
		Long sellerId = seller.getId();
		
		List<Product> products = productRepository.findAll();
		
		long totalProducts = products.stream().filter(p->sellerId.equals(p.getSellerId())).count();
		
		long totalEcoCertified = products.stream().filter(p->sellerId.equals(p.getSellerId())&&Boolean.TRUE.equals(p.getEcoCertified())).count();
		
		Double totalRevenue = orderItemRepository.getTotalRevenueBySeller(sellerId);
		
		Double totalCarbon = orderItemRepository.getTotalCarbonBySeller(sellerId);
		
		if(totalRevenue == null) {
			totalRevenue = 0.0;
		}
		
		if(totalCarbon == null) {
			totalCarbon = 0.0;
		}
		
		Long totalOrders = (long)orderItemRepository.findBySellerId(sellerId).size();
		
		return new SellerReport(
				sellerId, 
				seller.getName(),
				totalProducts,
				totalEcoCertified,
				totalOrders, 
				totalCarbon,
				totalRevenue);
	}
	
}