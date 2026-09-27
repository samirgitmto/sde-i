package com.gql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gql.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserId(Long userId);
}
