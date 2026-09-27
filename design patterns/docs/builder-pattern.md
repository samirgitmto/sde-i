# Builder Design Pattern

## Intent
The Builder pattern separates the construction of a complex object from its representation, allowing the same construction process to create different representations.

## Structure
- **Builder**: Specifies an abstract interface for creating parts of a Product object.
- **ConcreteBuilder**: Constructs and assembles parts of the product by implementing the Builder interface.
- **Director**: Constructs an object using the Builder interface.
- **Product**: Represents the complex object under construction.

## When to Use
- When the construction process of an object should be independent of the parts that make up the object and how they're assembled.
- When you need to create different representations of a complex object.

## Example Use Case
Building a complex `Meal` object in a restaurant ordering system, where a `MealBuilder` assembles different combinations of burgers, drinks, and sides, and a `Director` (e.g., a waiter) manages the construction process.

---

The Builder pattern is especially useful for constructing objects with many optional parts or configurations. 