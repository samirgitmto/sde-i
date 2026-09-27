package code.jpa.relationshipsFetch;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDto {
	private Long id;
	private LocalDate orderDate;
	private List<OrderItemsDto> items = new ArrayList<>();
	
	// Default constructor
	public OrderDto() {
	}
	
	// Constructor with parameters
	public OrderDto(Long id, LocalDate orderDate, List<OrderItemsDto> items) {
		this.id = id;
		this.orderDate = orderDate;
		this.items = items != null ? items : new ArrayList<>();
	}
	
	// Getters and Setters
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDate getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	
	public List<OrderItemsDto> getItems() {
		return items;
	}
	
	public void setItems(List<OrderItemsDto> items) {
		this.items = items != null ? items : new ArrayList<>();
	}
}
