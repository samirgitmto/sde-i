package code.basics.circularDependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

//	@Autowired
//	@Lazy
	private OrderService orderService;
	
	private static int LAPTOP = 10;
	
	public InventoryService() {
		System.err.println("InventoryService instantiated");
	}
	
	@Autowired
//	public InventoryService(OrderService orderService) {
	public InventoryService(@Lazy OrderService orderService) {
		this.orderService = orderService;
		System.err.println("InventoryService instantiated through parameterized constructor");
	}
	
	public int checkAvailability(String item) {
		if (item.equalsIgnoreCase("laptop"))
			return LAPTOP;
		else
			return 0;
	}
	
	public void updateInventory(String item, int qtySold) {
		LAPTOP -= qtySold;
		System.err.println("laptop quantity updated to " + LAPTOP);
		orderService.logOrderEvents(item, qtySold);
	}
}