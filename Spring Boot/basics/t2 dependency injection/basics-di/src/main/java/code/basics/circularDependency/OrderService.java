package code.basics.circularDependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	
//	@Autowired
//	@Lazy
	private InventoryService inventoryService;
	
	public OrderService() {
		System.err.println("OrderService instantiated");
	}
	
	@Autowired
//	public OrderService(@Lazy InventoryService inventoryService) {
	public OrderService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
		System.err.println("OrderService instantiated through parameterized constructor");
	}
	
	public String processOrder(String item, int qty) {
		int qtyAvailable = inventoryService.checkAvailability(item);
		if (qtyAvailable>=qty) {
			inventoryService.updateInventory(item, qty);
			return "order processed";
		}
		else
			return "insufficient stock";
		
	}
	
	public void logOrderEvents(String item, int qty) {
		System.err.println("order logged for item " + item + " with quantity " + qty);
	}
}