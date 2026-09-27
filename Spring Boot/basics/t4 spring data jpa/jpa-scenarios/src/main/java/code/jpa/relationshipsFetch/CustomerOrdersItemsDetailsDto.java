package code.jpa.relationshipsFetch;

import java.util.ArrayList;
import java.util.List;


public class CustomerOrdersItemsDetailsDto {
	private Long id;
	private String name;
    private List<OrderDto> orders;
    
    // Default constructor for JSON deserialization
    public CustomerOrdersItemsDetailsDto() {
    }
    
	public CustomerOrdersItemsDetailsDto(Long id, String name, List<OrderDto> orders) {
		super();
		this.id = id;
		this.name = name;
		this.orders = orders;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<OrderDto> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderDto> orders) {
		this.orders = orders;
	}
	
}
