package code.jpa.relationshipsFetch;

import java.time.LocalDate;
import java.util.List;

public class CustomerOrdersDto {

	private Long customerId;
    private String customerName;
    private List<OrderDtoRecord> orders;

    // Constructor + Getters/Setters (or use Lombok @Data)
    public static record OrderDtoRecord(
        Long orderId,
        LocalDate orderDate
        // List<OrderItemsDto> items
        // LazyInitializationException if you try to access order.getItems() (since Order.items is LAZY and the transaction is closed).
    ) {}

	public CustomerOrdersDto(Long customerId, String customerName, List<OrderDtoRecord> orders) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.orders = orders;
	}
    
    // Default constructor for JSON deserialization
    public CustomerOrdersDto() {
    }
    
    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public List<OrderDtoRecord> getOrders() {
        return orders;
    }
    
    public void setOrders(List<OrderDtoRecord> orders) {
        this.orders = orders;
    }
}