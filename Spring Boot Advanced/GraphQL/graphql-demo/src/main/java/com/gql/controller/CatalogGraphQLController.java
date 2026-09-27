package com.gql.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.gql.model.Order;
import com.gql.model.Product;
import com.gql.repository.OrderRepository;
import com.gql.repository.ProductRepository;

@Controller
public class CatalogGraphQLController {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	public CatalogGraphQLController(OrderRepository orderRepository, ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
	}

	@QueryMapping
	public Order order(@Argument Long id) {
		return orderRepository.findById(id).orElse(null);
	}

	@QueryMapping
	public List<Order> orders() {
		return orderRepository.findAll();
	}

	@QueryMapping
	public Product product(@Argument Long id) {
		return productRepository.findById(id).orElse(null);
	}

	@QueryMapping
	public List<Product> products() {
		return productRepository.findAll();
	}
}
