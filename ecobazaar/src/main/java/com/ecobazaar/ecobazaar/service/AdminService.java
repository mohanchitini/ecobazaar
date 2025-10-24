package com.ecobazaar.ecobazaar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ecobazaar.ecobazaar.model.Order;
import com.ecobazaar.ecobazaar.model.Product;
import com.ecobazaar.ecobazaar.model.User;
import com.ecobazaar.ecobazaar.repository.OrderRepository;
import com.ecobazaar.ecobazaar.repository.ProductRepository;
import com.ecobazaar.ecobazaar.repository.UserRepository;

@Service
public class AdminService {
	
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	
	public AdminService(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}
	
	public Product approveProduct(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("Product not found"));
		
		product.setEcoCertified(true);
		return productRepository.save(product);
	}
	
	public User approveSeller(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("User not Found"));
		user.setRole("SELLER");
		return userRepository.save(user);
		
	}

	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	public Map<String, Object> getAdminReport(){
		List<Order> orders = orderRepository.findAll();
		
		double totalCarbon = orders.stream().mapToDouble(Order::getTotalCarbon).sum();
		double totalRevenue = orders.stream().mapToDouble(Order::getTotalPrice).sum();
		
		Map<String, Object>report = new HashMap<>();
		report.put("totalOrders", orders.size());
		report.put("totalRevenue", totalRevenue);
		report.put("totalCarbonSaved", totalCarbon);
		report.put("totalUsers", userRepository.count());
		report.put("totalProducts", productRepository.count());
		
		return report;
	}
	
	public String generateReportCSV() {
		List<Order> order = orderRepository.findAll();
		
		StringBuilder csv = new StringBuilder("OrderId, UserId, TotalPrice, TotalCarbon\n");
		
		for(Order o:order) {
			csv.append(o.getId()).append(",")
			.append(o.getUserId()).append(",")
			.append(o.getTotalPrice()).append(",")
			.append(o.getTotalCarbon()).append("\n");
	}
		return csv.toString();
  }
}