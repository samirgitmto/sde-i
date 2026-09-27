package com.gql.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.gql.model.Order;
import com.gql.model.Product;
import com.gql.model.User;
import com.gql.repository.OrderRepository;
import com.gql.repository.ProductRepository;

/**
 * Nested field resolvers for User → Orders → Products.
 * Each call hits the DB per parent — classic N+1 when querying many parents.
 *
 * Example: query { users { orders { products { name } } } }
 *   1 query for users
 *   + N queries for orders (one per user)
 *   + M queries for products (one per order)
 */
@Controller
public class RelationshipController {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	public RelationshipController(OrderRepository orderRepository, ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
	}

	@SchemaMapping(typeName = "User", field = "orders")
	public List<Order> orders(User user) {
		return orderRepository.findByUserId(user.getId());
	}

	@SchemaMapping(typeName = "Order", field = "products")
	public List<Product> products(Order order) {
		return productRepository.findByOrderId(order.getId());
	}
}
