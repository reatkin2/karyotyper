package basicObjects;

import java.awt.Color;
import java.awt.Point;

public class Pixel {
	private Point point;
	private Color color;

	public Pixel() {
		this.point = new Point(-1, -1);
		this.color = new Color(0, 0, 0);
	}

	public Pixel(Point coord, Color color) {
		this.point = new Point(coord);
		this.color = new Color(color.getRGB());
	}

	public Color getColor() {
		return new Color(this.color.getRGB());
	}

	public Point getPoint() {
		return new Point(this.point);
	}

	public void setPoint(int x, int y) {
		this.point = new Point(x, y);
	}

	public void setColor(Color newColor) {
		this.color = new Color(newColor.getRGB());
	}
}
