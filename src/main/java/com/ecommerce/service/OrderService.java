package com.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.domain.Customer;
import com.ecommerce.domain.Item;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.OrderStatus;
import com.ecommerce.domain.Payment;
import com.ecommerce.domain.Product;
import com.ecommerce.entity.ItemEntity;
import com.ecommerce.entity.OrderEntity;
import com.ecommerce.entity.ProductEntity;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CustomerRepository customerRepository;

	public Order getOrder(Long id) throws Exception {
		log.info("Getting order by id {}", id);
		Order orderDomain = null;
		Optional<OrderEntity> order = orderRepository.findById(id);
		if (order.isPresent()) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			orderDomain = mapper.convertValue(order.get(), Order.class);
			Customer customer = mapper.convertValue(order.get().getCustomer(), Customer.class);
			orderDomain.setCustomer(customer);
			if (ObjectUtils.isNotEmpty(orderDomain)) {
				if (ObjectUtils.isNotEmpty(orderDomain.getItems())) {
					for (Item item : orderDomain.getItems()) {
						Optional<ProductEntity> product = productRepository.findById(item.getProductId());
						if (product.isPresent()) {
							item.setProduct(mapper.convertValue(product.get(), Product.class));
						}
					}
				}

			}
		}

		return orderDomain;
	}

	@Transactional
	public Order createOrder(Order order) throws Exception {
		log.debug("Adding new order");
		if (ObjectUtils.isNotEmpty(order)) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			OrderEntity orderEntity = mapper.convertValue(order, OrderEntity.class);
			validateOrder(orderEntity);
			orderEntity.setStatus(OrderStatus.PROCESSING);
			OrderEntity saved = orderRepository.save(orderEntity);
			log.info("Created order {} for customer {}",saved.getId(), saved.getCustomer().getId());
			processPayment(saved.getId(), order.getPayments());
			order = mapper.convertValue(saved, Order.class);
		}
		return order;
	}

	private void processPayment(Long orderId, List<Payment> payments) {
		/**
		 * call payment microservice to execute all payments for the given orderId(in
		 * case multiple cards are involved)
		 * 
		 * if either payment fails 
		 * 1.Update OrderStatus from PROCESSING to PAYMENT_FAILED, and trigger a UI event with failed response 
		 * 2.Same payment options could be used with a retry and change the order status accordingly
		 * 
		 * else if all payments are successful 
		 * 1.Update order with ORDERED status
		 * 2.Trigger a UI event with success response
		 * 
		 */
	}

	private void validateOrder(OrderEntity orderEntity) throws Exception {
		log.debug("Validating order");
		if (ObjectUtils.isEmpty(orderEntity.getCustomer()) || ObjectUtils.isEmpty(orderEntity.getItems())) {
			throw new Exception("Invalid Order: cutomer and/or items is not present");
		} else {
			if (ObjectUtils.isEmpty(orderEntity.getCustomer().getCustomerId()) || ObjectUtils
					.isEmpty(customerRepository.findByCustomerId(orderEntity.getCustomer().getCustomerId()))) {
				throw new Exception("Invalid Customer");
			}
			orderEntity.setCustomer(customerRepository.findByCustomerId(orderEntity.getCustomer().getCustomerId()));
			for (ItemEntity item : orderEntity.getItems()) {
				if (ObjectUtils.isEmpty(item.getProductId())
						|| (!productRepository.findById(item.getProductId()).isPresent())) {
					throw new Exception("Invalid Product with id " + item.getProductId());
				} else {
					ProductEntity product = productRepository.findById(item.getProductId()).get();
					item.setTotal(item.getTotal() + item.getQuantity() * product.getPrice());
				}
			}
		}
	}

	@Transactional
	public Order cancelOrder(Long id) {
		log.info("Cancelling order {}",id);
		Optional<OrderEntity> order = orderRepository.findById(id);
		Order orderDomain = null;
		if (order.isPresent()) {
			order.get().setStatus(OrderStatus.CANCELLED);
			orderRepository.save(order.get());
			updatePayment(id);
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			orderDomain = mapper.convertValue(order.get(), Order.class);
		}
		return orderDomain;
	}
	

	private void updatePayment(Long id) {
		/**
		 * call payment microservice to 
		 * 1.Revert all payments for the orderId 
		 * (or could be done periodically with a job by looking at the CANCELLED state)
		 * 2.Delete the order cascading deleting all attached entites.
		 */
	}

	public boolean createBulkOrder(List<Order> orders) {
		boolean ack = true;
		try {
			for (Order order : orders) {
				createOrder(order);
			}
		} catch (Exception e) {
			log.error("Exception:", e);
		}
		return ack;
	}

}
