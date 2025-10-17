package com.ecobazaar.ecobazaar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazaar.ecobazaar.model.Order;
import com.ecobazaar.ecobazaar.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;
	
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@PostMapping("/checkout/{userId}")
	public Order checkout(@PathVariable Long userId) {
		return orderService.checkout(userId);
	}
	
	@GetMapping("/{userId}")
	public List<Order> getUserOrder(@PathVariable Long userId){
		return orderService.getOrdersByUserId(userId);
	}
}