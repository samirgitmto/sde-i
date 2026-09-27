package fac.demo1.basic;

public class ShapeFactory {

	public static Shape getShape(String shape) {
		switch (shape) {
		case "CIRCLE":
			return new Circle();
		case "SQUARE":
			return new Square();
		default:
			throw new IllegalArgumentException("Unexpected value: " + shape);
		}
	}
	
}
