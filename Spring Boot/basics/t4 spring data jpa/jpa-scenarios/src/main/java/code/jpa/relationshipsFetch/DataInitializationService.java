package code.jpa.relationshipsFetch;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Service
@Transactional
public class DataInitializationService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * Initialize database with sample data for Customer, Order, and OrderItem entities
     * This method will be called after the Spring context is initialized
     */
    @PostConstruct
    public void initializeData() {
        // Check if data already exists to avoid duplicate initialization
        if (customerRepository.count() > 0) {
            System.out.println("Database already contains data. Skipping initialization.");
            return;
        }
        
        System.out.println("Initializing database with sample data...");
        
        // Create Customers
        Customer customer1 = createCustomer("John Doe");
        Customer customer2 = createCustomer("Jane Smith");
        Customer customer3 = createCustomer("Bob Johnson");
        
        // Create Orders for Customer 1
        Order order1 = createOrder(customer1, LocalDate.of(2024, 1, 15));
        Order order2 = createOrder(customer1, LocalDate.of(2024, 2, 20));
        
        // Create Orders for Customer 2
        Order order3 = createOrder(customer2, LocalDate.of(2024, 1, 10));
        Order order4 = createOrder(customer2, LocalDate.of(2024, 3, 5));
        
        // Create Orders for Customer 3
        Order order5 = createOrder(customer3, LocalDate.of(2024, 2, 28));
        
        // Create Order Items for Order 1
        createOrderItem(order1, "Laptop", 1);
        createOrderItem(order1, "Mouse", 2);
        createOrderItem(order1, "Keyboard", 1);
        
        // Create Order Items for Order 2
        createOrderItem(order2, "Monitor", 1);
        createOrderItem(order2, "Headphones", 1);
        
        // Create Order Items for Order 3
        createOrderItem(order3, "Smartphone", 1);
        createOrderItem(order3, "Phone Case", 2);
        createOrderItem(order3, "Screen Protector", 3);
        
        // Create Order Items for Order 4
        createOrderItem(order4, "Tablet", 1);
        createOrderItem(order4, "Stylus", 1);
        
        // Create Order Items for Order 5
        createOrderItem(order5, "Gaming Console", 1);
        createOrderItem(order5, "Controller", 2);
        createOrderItem(order5, "Game", 3);
        createOrderItem(order5, "HDMI Cable", 1);
        
        System.out.println("Database initialization completed successfully!");
        System.out.println("Created: " + customerRepository.count() + " customers");
        System.out.println("Created: " + orderRepository.count() + " orders");
        System.out.println("Created: " + orderItemRepository.count() + " order items");
    }
    
    /**
     * Alternative initialization method using CommandLineRunner
     * This can be used instead of @PostConstruct if you need more control
     */
    // @Component
    public class DataInitializer implements CommandLineRunner {
        
        @Override
        public void run(String... args) throws Exception {
            initializeData();
        }
    }
    
    // Helper methods for creating entities
    private Customer createCustomer(String name) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setOrders(new ArrayList<>());
        return customerRepository.save(customer);
    }
    
    private Order createOrder(Customer customer, LocalDate orderDate) {
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setCustomer(customer);
        order.setItems(new ArrayList<>());
        
        // Add order to customer's order list
        customer.getOrders().add(order);
        
        return orderRepository.save(order);
    }
    
    private OrderItem createOrderItem(Order order, String productName, int quantity) {
        OrderItem item = new OrderItem();
        item.setProductName(productName);
        item.setQuantity(quantity);
        item.setOrder(order);
        
        // Add item to order's item list
        order.getItems().add(item);
        
        return orderItemRepository.save(item);
    }
    
    /**
     * Method to clear all data (useful for testing)
     */
    public void clearAllData() {
        System.out.println("Clearing all data from database...");
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        System.out.println("All data cleared successfully!");
    }
    
    /**
     * Method to reinitialize data (clear and recreate)
     */
    public void reinitializeData() {
        clearAllData();
        initializeData();
    }
} 