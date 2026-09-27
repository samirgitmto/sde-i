package fac.demo2.pure;

public class CircleFactory implements ShapeFactory {

	@Override
	public Shape createShape() {
		return new Circle();
	}

}
