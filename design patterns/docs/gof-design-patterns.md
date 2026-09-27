# Gang of Four (GoF) Design Patterns

The GoF design patterns are 23 classic software design patterns introduced in the book "Design Patterns: Elements of Reusable Object-Oriented Software" by Erich Gamma, Richard Helm, Ralph Johnson, and John Vlissides.

## Creational Patterns

1. **Abstract Factory**: Provides an interface for creating families of related or dependent objects without specifying their concrete classes.
2. **Builder**: Separates the construction of a complex object from its representation, allowing the same construction process to create different representations.
3. **Factory Method**: Defines an interface for creating an object, but lets subclasses alter the type of objects that will be created.
4. **Prototype**: Creates new objects by copying an existing object, known as the prototype.
5. **Singleton**: Ensures a class has only one instance and provides a global point of access to it.

## Structural Patterns

6. **Adapter**: Allows incompatible interfaces to work together by converting the interface of a class into another interface clients expect.
7. **Bridge**: Decouples an abstraction from its implementation so that the two can vary independently.
8. **Composite**: Composes objects into tree structures to represent part-whole hierarchies, allowing clients to treat individual objects and compositions uniformly.
9. **Decorator**: Adds additional responsibilities to an object dynamically without altering its structure.
10. **Facade**: Provides a simplified interface to a complex subsystem.
11. **Flyweight**: Reduces memory usage by sharing as much data as possible with similar objects.
12. **Proxy**: Provides a surrogate or placeholder for another object to control access to it.

## Behavioral Patterns

13. **Chain of Responsibility**: Passes a request along a chain of handlers, allowing each handler to process the request or pass it along the chain.
14. **Command**: Encapsulates a request as an object, thereby allowing for parameterization and queuing of requests.
15. **Interpreter**: Defines a grammar for a language and provides an interpreter to deal with this grammar.
16. **Iterator**: Provides a way to access the elements of an aggregate object sequentially without exposing its underlying representation.
17. **Mediator**: Defines an object that encapsulates how a set of objects interact, promoting loose coupling.
18. **Memento**: Captures and externalizes an object's internal state without violating encapsulation, so the object can be restored to this state later.
19. **Observer**: Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.
20. **State**: Allows an object to alter its behavior when its internal state changes.
21. **Strategy**: Defines a family of algorithms, encapsulates each one, and makes them interchangeable.
22. **Template Method**: Defines the skeleton of an algorithm in a method, deferring some steps to subclasses.
23. **Visitor**: Represents an operation to be performed on elements of an object structure, allowing new operations to be defined without changing the classes of the elements. 