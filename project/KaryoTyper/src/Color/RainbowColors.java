package color;

import java.awt.Color;

public class RainbowColors {
	public static Color tasteRainbow(int color){
		switch(color){
		case 0:
			return new Color(Color.MAGENTA.getRGB());
		case 1:
			return new Color(Color.orange.getRGB());
		case 2:
			return new Color(Color.yellow.getRGB());
		case 3:
			return new Color(Color.green.getRGB());
		case 4:
			return new Color(Color.blue.getRGB());
		case 5:
			return new Color(Color.CYAN.getRGB());
		default:
			return new Color(Color.pink.getRGB());
		}
	}
}
