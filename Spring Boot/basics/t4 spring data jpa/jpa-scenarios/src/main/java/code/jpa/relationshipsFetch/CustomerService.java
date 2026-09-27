package code.jpa.relationshipsFetch;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository customerRepo;
	
	public CustomerOrdersItemsDetailsDto getCustomerDashboardV1(Long customerId) {
//		Optional<Customer> customerOptional = this.customerRepo.findCustomerWithOrders(customerId);   // N+1
		
		Optional<Customer> customerOptional = this.customerRepo.findCustomerWithOrders(customerId);
//		org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags:
//		[code.jpa.relationshipsFetch.Order.items, code.jpa.relationshipsFetch.Customer.orders]
		if (customerOptional.isEmpty()) {
			System.err.println("no customer found");
			return null;
		}
		
		Customer customer = customerOptional.get();
		
		List<OrderDto> orderDtos = customer.getOrders().stream()
				.map(o -> {
					List<OrderItemsDto> items = o.getItems().stream()
							.map(i -> {
								return new OrderItemsDto(
										i.getId(),
										i.getProductName(),
										i.getQuantity()
								);
							}).toList();
					return new OrderDto(o.getId(), o.getOrderDate(), items);
				}).collect(Collectors.toList());
		
		CustomerOrdersItemsDetailsDto dto = new CustomerOrdersItemsDetailsDto(customerId,
				customer.getName(),
				orderDtos);
		
		return dto;
	}
	
//	public CustomerOrdersItemsDetailsDto getCustomerDashboard(Long customerId) {
	public Customer getCustomerDashboard(Long customerId) {
		
		Customer customer = this.customerRepo.findCustomerWithOrders2(customerId);
		
		List<Order> orders = this.customerRepo.findCustomerOrderItems(customerId);
		
		return customer;
	}
	
	public CustomerOrdersDto getCustomerOrders(Long customerId) {
		Optional<Customer> customer = this.customerRepo.findCustomerWithOrders(customerId);
		if (customer.isEmpty()) {
			System.err.println("no customer found");
			return null;
		}
		Customer cus = customer.get();
		CustomerOrdersDto customerOrdersDto = new CustomerOrdersDto(cus.getId(),
				cus.getName(),
				cus.getOrders().stream().map(o -> new CustomerOrdersDto.OrderDtoRecord(o.getId(), o.getOrderDate())).toList());
		
		return customerOrdersDto;		
	}
	/*
	public Optional<CustomerOrdersDto> getCustomerOrders(Long customerId) {
		Optional<Customer> customerOpt = this.customerRepo.findCustomerWithOrders(customerId);
		return customerOpt.map(customer -> {
			List<CustomerOrdersDto.OrderDto> orderDtos = customer.getOrders().stream()
				.map(order -> new CustomerOrdersDto.OrderDto(order.getId(), order.getOrderDate()))
				.collect(Collectors.toList());
			return new CustomerOrdersDto(customer.getId(), customer.getName(), orderDtos);
		});
	}
	 */
}