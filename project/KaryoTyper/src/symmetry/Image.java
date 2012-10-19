package symmetry;

public class Image {

	private short[][] imgColorValues;

	public Image(short[][] imgColorValues) {
		this.imgColorValues = imgColorValues;
	}

	public Image(boolean[][] booleanImg) {
		imgColorValues = new short[booleanImg.length][booleanImg[0].length];
		for (int i = 0; i < booleanImg.length; i++) {
			for (int j = 0; j < booleanImg[0].length; j++) {
				imgColorValues[i][j] = booleanImg[i][j] ? (short) 1 : (short) 0;
			}
		}
	}

	public short[][] getImageMap() {
		return imgColorValues;
	}

	public int getColorAt(int i, int j) {
		try {
			return imgColorValues[i][j];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	public String toString() {
		String imgString = "";
		for (int i = 0; i < imgColorValues.length; i++) {
			for (int j = 0; j < imgColorValues[0].length; j++) {
				imgString += imgColorValues[i][j] + " ";
			}
			imgString += "\n";
		}
		return imgString;
	}

}
