package code.jpa.relationshipsFetch;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("Select c From Customer c Left JOIN FETCH c.orders o Where c.id = :cId")
	Optional<Customer> findCustomerWithOrders(@Param("cId") Long customerId);

	// MultipleBagFetchException
	@Query("Select c From Customer c Left JOIN FETCH c.orders o Left JOIN FETCH o.items i Where c.id = :cId")
	Optional<Customer> findCustomerOrdersSummary(@Param(value = "cId") Long custmerId);
	
	// Workaround for MultipleBagFetchException: use findCustomerWithOrders & findCustomerOrderItems
	@Query("Select o From Order o Left JOIN FETCH o.items i Where o.customer.id = :cId")
	List<Order> findCustomerOrderItems(@Param("cId") Long customerId);
	@Query("Select c From Customer c Left JOIN FETCH c.orders o Where c.id = :cId")
	Customer findCustomerWithOrders2(@Param("cId") Long customerId);
}