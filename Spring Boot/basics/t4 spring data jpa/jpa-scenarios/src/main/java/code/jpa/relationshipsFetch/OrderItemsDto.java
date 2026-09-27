package code.jpa.relationshipsFetch;

public class OrderItemsDto {

	private Long id;
	private String productName;
    private int quantity;
    
    // Default constructor for JSON deserialization
    public OrderItemsDto() {
    }
    
	public OrderItemsDto(Long id, String productName, int quantity) {
		super();
		this.id = id;
		this.productName = productName;
		this.quantity = quantity;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
    
}