Scenario 2: Handling Circular Dependency and Choosing Injection Style
Scenario:
You have two services: OrderService and InventoryService. OrderService checks inventory before placing an order, while InventoryService logs events after order placement. Both use each other. You implemented field injection and get a circular dependency error.

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