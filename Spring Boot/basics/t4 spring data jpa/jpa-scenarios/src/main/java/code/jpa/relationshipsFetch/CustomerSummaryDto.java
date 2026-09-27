package code.jpa.relationshipsFetch;

import java.time.LocalDate;
import java.util.List;

public class CustomerSummaryDto {
    private Long customerId;
    private String customerName;
    private int totalOrders;
    private int totalItems;
    private LocalDate lastOrderDate;
    private List<OrderSummaryDto> recentOrders;
    
    public static class OrderSummaryDto {
        private Long orderId;
        private LocalDate orderDate;
        private int itemCount;
        private int totalQuantity;
        
        public OrderSummaryDto() {}
        
        public OrderSummaryDto(Long orderId, LocalDate orderDate, int itemCount, int totalQuantity) {
            this.orderId = orderId;
            this.orderDate = orderDate;
            this.itemCount = itemCount;
            this.totalQuantity = totalQuantity;
        }
        
        // Getters and Setters
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        
        public LocalDate getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
        
        public int getItemCount() { return itemCount; }
        public void setItemCount(int itemCount) { this.itemCount = itemCount; }
        
        public int getTotalQuantity() { return totalQuantity; }
        public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    }
    
    // Constructors
    public CustomerSummaryDto() {}
    
    public CustomerSummaryDto(Long customerId, String customerName, int totalOrders, 
                            int totalItems, LocalDate lastOrderDate, List<OrderSummaryDto> recentOrders) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.totalOrders = totalOrders;
        this.totalItems = totalItems;
        this.lastOrderDate = lastOrderDate;
        this.recentOrders = recentOrders;
    }
    
    // Getters and Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
    
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    
    public LocalDate getLastOrderDate() { return lastOrderDate; }
    public void setLastOrderDate(LocalDate lastOrderDate) { this.lastOrderDate = lastOrderDate; }
    
    public List<OrderSummaryDto> getRecentOrders() { return recentOrders; }
    public void setRecentOrders(List<OrderSummaryDto> recentOrders) { this.recentOrders = recentOrders; }
} 