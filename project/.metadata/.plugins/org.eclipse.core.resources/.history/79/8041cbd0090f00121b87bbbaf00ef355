package Color;

import java.awt.Color;

public class ISOClineColor {
	// 0-20 will be 0-256
	private static double scalingFactor = 12.75;


	public static Color getColor(int color){
		int rgb=(int)Math.round(ISOClineColor.scalingFactor*color);
		if(color>20){
			return new Color(255,255,255);
		}
		return new Color(rgb,rgb,rgb);

	}
}
