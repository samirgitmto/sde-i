package fac.demo2.pure;

public class SquareFactory implements ShapeFactory {

	@Override
	public Shape createShape() {
		return new Square();
	}
	
}
