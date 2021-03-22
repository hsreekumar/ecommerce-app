package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.Order;
import com.ecommerce.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@CrossOrigin
@RequestMapping("/order")
@Api(value = "Order Api")
public class OrderController {

	@Autowired
	OrderService orderService;


	/**
	 * Return Order Details by Id
	 * 
	 * @param OrderId
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "Get order", response = Order.class)
	@GetMapping("/{orderId}")
	public Order getOrder(@PathVariable("orderId") Long orderId) throws Exception {
		return orderService.getOrder(orderId);
	}
	
	/**
	 * Create order
	 * 
	 * @param order
	 * @return Order
	 * @throws Exception 
	 */
	@ApiOperation(value = "Create order", response = Order.class)
	@PostMapping
	public Order createOrder(@RequestBody Order order) throws Exception {
		return orderService.createOrder(order);
	}
	
	/**
	 * Cancel order
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Cancel order", response = Order.class)
	@PutMapping("/cancel/{id}")
	public Order cancelOrder(@PathVariable Long id) throws Exception {
		return orderService.cancelOrder(id);
	}
	
	/**
	 * 
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Create bulk orders", response = Boolean.class)
	@PostMapping("/bulk")
	public boolean addBulkOrder(@RequestBody List<Order> orders) throws Exception {
		boolean ack = false;
		ack = orderService.createBulkOrder(orders);
		return ack;
	}
	
}