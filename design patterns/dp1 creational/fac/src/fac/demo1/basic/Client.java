package fac.demo1.basic;

public class Client {

	public static void main(String[] args) {
		Shape circle = ShapeFactory.getShape("CIRCLE");
		circle.draw();
	}
	
}
