/**
 * 
 */
package runner;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import color.PixelColor;

import characterize.Characterizer;
import characterize.GrayBuffer;

/**
 * @author ahkeslin
 * 
 */
public class Linearizer {
	public static void main(String[] args) {
		try {
			String inputPath = args[0];
			BufferedImage img = ImageIO.read(new File(inputPath));
			ArrayList<Point> medialAxis = new ArrayList<Point>();
			GrayBuffer chromBuffer = new GrayBuffer(img.getWidth(), img.getHeight());

			// Opaque pure black
			int PURE_BLACK = 0 | Color.OPAQUE;

			// Read through image for red pixels
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					// Returned integer is really Alpha, Red, Green, Blue with
					// each having it's own 8 bits so use AWT's Color class to simplify
					Color pixelValue = new Color(img.getRGB(x, y));
					int red = pixelValue.getRed();
					int green = pixelValue.getGreen();
					int blue = pixelValue.getBlue();
					int alpha = pixelValue.getAlpha();

					if (alpha < 1) {
						chromBuffer.set(new Point(x, y), -1);
					} else {
						chromBuffer.set(new Point(x, y), PixelColor.colorToGrayscale(pixelValue));
					}

					// Find red pixels as they represent the medial axis and add
					// them to our medialAxis
					if (red < 50 && green < 50 && blue > 150) {
						medialAxis.add(new Point(x, y));
					}
				}
			}

			GrayBuffer linearizedChrom = Characterizer.linearizeChromosome(chromBuffer, medialAxis);
			BufferedImage linearizedImage = new BufferedImage(linearizedChrom.width,
					linearizedChrom.height, BufferedImage.TYPE_BYTE_GRAY);
			double[] bandFunction = Characterizer.calculateBandFunction(linearizedChrom);
			for (int i = 0; i < bandFunction.length; i++) {
				System.out.println(bandFunction[i]);
			}

			for (int x = 0; x < linearizedImage.getWidth(); x++) {
				for (int y = 0; y < linearizedImage.getHeight(); y++) {
					int grey = linearizedChrom.get(x, y);
					int rgb;

					if (grey == -1) {
						rgb = 255 * 0x00010101;
					} else {
						rgb = grey * 0x00010101;
					}

					linearizedImage.setRGB(x, y, rgb);
				}
			}

			String currentPath = (new File(".")).getCanonicalPath();
			String outputPath = currentPath + File.separator + "shapeData" + File.separator
					+ "TestData" + File.separator + "TestLinearize.png";
			File outputFile = new File(outputPath);
			ImageIO.write(linearizedImage, "png", outputFile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
