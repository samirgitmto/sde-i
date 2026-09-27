package com.gql.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gql.model.Order;
import com.gql.model.Product;
import com.gql.model.User;
import com.gql.repository.OrderRepository;
import com.gql.repository.ProductRepository;
import com.gql.repository.UserRepository;

@Configuration
public class SampleDataConfig {

	@Bean
	CommandLineRunner loadSampleData(
			UserRepository userRepository,
			OrderRepository orderRepository,
			ProductRepository productRepository) {
		return args -> {
			if (userRepository.count() > 0) {
				return;
			}

			Product laptop = productRepository.save(new Product("Laptop", new BigDecimal("75000")));
			Product mouse = productRepository.save(new Product("Mouse", new BigDecimal("500")));
			Product keyboard = productRepository.save(new Product("Keyboard", new BigDecimal("1500")));
			Product monitor = productRepository.save(new Product("Monitor", new BigDecimal("12000")));

			User john = userRepository.save(new User("John", "john@example.com"));
			User jane = userRepository.save(new User("Jane", "jane@example.com"));
			User alex = userRepository.save(new User("Alex", "alex@example.com"));

			Order johnOrder1 = new Order(new BigDecimal("75500"), john);
			johnOrder1.setProducts(List.of(laptop, mouse));
			orderRepository.save(johnOrder1);

			Order johnOrder2 = new Order(new BigDecimal("1500"), john);
			johnOrder2.setProducts(List.of(keyboard));
			orderRepository.save(johnOrder2);

			Order janeOrder = new Order(new BigDecimal("12000"), jane);
			janeOrder.setProducts(List.of(monitor));
			orderRepository.save(janeOrder);

			Order alexOrder = new Order(new BigDecimal("12500"), alex);
			alexOrder.setProducts(List.of(monitor, mouse));
			orderRepository.save(alexOrder);
		};
	}
}
