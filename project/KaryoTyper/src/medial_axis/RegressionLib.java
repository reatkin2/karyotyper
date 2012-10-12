/**
 * 
 */
package medial_axis;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.optimization.fitting.PolynomialFitter;
import org.apache.commons.math3.optimization.general.LevenbergMarquardtOptimizer;

/**
 * @author ahkeslin
 * 
 */
public class RegressionLib {
	// This is set after running a regression method in this class.
	private double fitError;

	public static void main(String[] args) {
		try {
			int DEGREE = 3;
			String inputPath = args[0];
			BufferedImage img = ImageIO.read(new File(inputPath));
			ArrayList<Point> points = new ArrayList<Point>();

			// Read through image for red pixels
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					// Returned integer is really Alpha, Red, Green, Blue with
					// each having it's own 8 bits so use AWT's Color class to simplify
					Color pixelValue = new Color(img.getRGB(x, y));
					int red = pixelValue.getRed();
					int green = pixelValue.getGreen();
					int blue = pixelValue.getBlue();

					// Find red pixels as they represent the medial axis and add
					// them to our fitter
					if (red > 0 && green < 50 && blue < 50) {
						points.add(new Point(x, y));
					}
				}
			}

			RegressionLib regressor = new RegressionLib();
			DifferentiableUnivariateFunction approximation = regressor.approxByPolynomial(DEGREE,
					points);
			System.out.printf("RMS error: %s\n", regressor.getFitError());

			// Opaque pure blue
			int PURE_BLUE = 0xff | Color.OPAQUE;

			// Write out our new approximation onto the buffer in blue.
			for (int x = 0; x < img.getWidth(); x++) {

				int y = (int) approximation.value(x);

				if (y >= img.getHeight() - 1 || y < 0) {
					continue;
				}

				img.setRGB(x, y, PURE_BLUE);
			}

			String currentPath = (new File(".")).getCanonicalPath();
			String outputPath = currentPath + File.separator + "shapeData" + File.separator
					+ "TestData" + File.separator + "TestRegression.png";
			File outputFile = new File(outputPath);
			ImageIO.write(img, "png", outputFile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public DifferentiableUnivariateFunction approxByPolynomial(int degree, ArrayList<Point> points) {
		LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();
		PolynomialFitter fitter = new PolynomialFitter(degree, optimizer);

		for (Point pt : points) {
			fitter.addObservedPoint(pt.x, pt.y);
		}

		PolynomialFunction approximation = new PolynomialFunction(fitter.fit());
		this.setFitError(optimizer.getRMS());
		return approximation;
	}

	public double getFitError() {
		return fitError;
	}

	public void setFitError(double fitError) {
		this.fitError = fitError;
	}
}
