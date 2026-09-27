package fac.demo2.pure;

/**
 The Factory Pattern is used when:
You want to delegate object creation to subclasses or separate factories.
Each factory creates one type (or “family member”) of product — e.g., CircleFactory → Circle, SquareFactory → Square.
It helps with object creation encapsulation and OCP compliance, but each factory is focused on a single product.

 * @since 04-11-2025 
 */
public class Client {

	/**
	 client depends only on abstractions (ShapeFactory and Shape), not concrete product or factory implementations — exactly how the Factory Pattern
	  should be done under SOLID principles.
	 */
	public static void main(String[] args) {
		ShapeFactory shapeFactory = new CircleFactory();
		Shape circle = shapeFactory.createShape();
		circle.draw();
	}
	
}