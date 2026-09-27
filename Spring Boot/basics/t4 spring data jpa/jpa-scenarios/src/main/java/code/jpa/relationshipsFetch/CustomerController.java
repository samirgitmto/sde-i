package code.jpa.relationshipsFetch;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

	private CustomerService customerService;
	
	public CustomerController(CustomerService cS) {
		this.customerService = cS;
	}
	
//	0. Fetch all orders for a customer (with all orders only)
	@GetMapping("/customers/{customerId}")
	public ResponseEntity<CustomerOrdersDto> getCustomerOrders(@PathVariable Long customerId) {
		CustomerOrdersDto res = this.customerService.getCustomerOrders(customerId);
		return ResponseEntity.ok(res);
	}
	
//	1. Fetch all orders for a customer (with all orders and items)
	@GetMapping("/customers/{customerId}/v1/orders")
	public ResponseEntity<CustomerOrdersItemsDetailsDto> getCustomerDashboard1(@PathVariable Long customerId) {
		CustomerOrdersItemsDetailsDto dto = this.customerService.getCustomerDashboardV1(customerId);	
		return ResponseEntity.ok(dto);
	}
	@GetMapping("/customers/{customerId}/v2/orders")
	public ResponseEntity<Customer> getCustomerDashboard2(@PathVariable Long customerId) {
		Customer dto = this.customerService.getCustomerDashboard(customerId);
		return ResponseEntity.ok(dto);
	}
}