package com.gql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gql.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select p from Order o join o.products p where o.id = :orderId")
	List<Product> findByOrderId(@Param("orderId") Long orderId);
}
