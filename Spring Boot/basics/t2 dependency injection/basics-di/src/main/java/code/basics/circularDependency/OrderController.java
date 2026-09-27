package code.basics.circularDependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
Scenario 2: Handling Circular Dependency and Choosing Injection Style
Scenario:
You have two services: OrderService and InventoryService. OrderService checks inventory before placing an order, while InventoryService logs events after order
 placement. Both use each other. You implemented field injection and get a circular dependency error.

How would you resolve this?

What the interviewer expects:
Understanding of constructor vs setter vs field injection
Why constructor injection fails in circular cases
Refactoring options:
	Using setter injection or @Lazy
	Splitting responsibilities to a mediator class (cleaner architecture)
Ability to discuss pros/cons of different injection styles

Follow-up questions:
Which injection style do you prefer and why?
How does Spring internally resolve circular dependencies?
 */

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping("/order/{item}")
	public ResponseEntity<String> placeOrder(@PathVariable String item, @RequestParam int qty) {
		
		String res = orderService.processOrder(item, qty);
		
		return ResponseEntity.ok(res);
	}
	
}
