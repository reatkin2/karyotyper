package idiogram;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import characterization.ChromosomeBand;
import characterization.ChromosomeBand.Type;

/**
 * Wrapper for a Java HashMap that maps an idiogram to a chromosome number.
 * 
 * ChromosomeIdiogramSheet.csv must be formatted as follows:
 * 
 * "Chromosome <number>,<resolution>"(",<type = white, black, grey, centromere> <width>")*
 * 
 * Easiest way is to input each idiogram into a spreadsheet, one idiogram per row. The first column
 * should contain "Chromosome <number>", second column resolution, remaining rows contain the bands
 * in type-width pairs preferably formatted as "<type> <width>" or "<type>,<width>", that's 1 space
 * or 1 comma.
 * 
 * Next save or export the spreadsheet as a comma separated version (.csv) and place in the project
 * folder.
 * 
 * @author Robert
 * 
 */
public class IdiogramMap {
	public final int NUM_IDIOGRAMS = 69;

	/** Maps idiogram to chromosome number. */
	private HashMap<Integer, Idiogram> idiogramMap;

	private Scanner fileReader;

	/**
	 * Initializes the HashMap
	 * 
	 * @param file
	 *            csv file to parse - ".\\ChromosomeIdiogramSheet.csv"
	 */
	public IdiogramMap(File file) {
		idiogramMap = new HashMap<Integer, Idiogram>(NUM_IDIOGRAMS);
		idiogramSheetParser(file);
	}

	/**
	 * Parses ChromosomeIdiogramSheet.csv to build idiogram map
	 */
	private void idiogramSheetParser(File file) {
		try {
			fileReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist.");
			e.printStackTrace();
		}

		String token = null;
		int chromosomeNumber = 0;
		ChromosomeBand.Type bandType;
		int bandLength = 0;
		ChromosomeBand band = null;
		int resolution = 0;
		Idiogram newIdiogram = null;
		String textLine = "";
		Scanner lineReader;

		while (fileReader.hasNextLine()) {
			textLine = fileReader.nextLine();
			lineReader = new Scanner(textLine);
			lineReader.useDelimiter(Pattern.compile("[\\\",\\s]"));

			// Debug code
			// System.out.println("\n" + textLine);
			// End debug

			while (lineReader.hasNext()) {
				token = lineReader.next();

				// Debug code
				// System.out.print("@" + token + ", ");
				// End debug

				if (token.toLowerCase().equals("white")) {
					bandType = Type.WHITE;
					bandLength = lineReader.nextInt();
					band = new ChromosomeBand(bandType, bandLength);
					newIdiogram.add(band);
				} else if (token.toLowerCase().equals("black")) {
					bandType = Type.BLACK;
					bandLength = lineReader.nextInt();
					band = new ChromosomeBand(bandType, bandLength);
					newIdiogram.add(band);
				} else if (token.toLowerCase().equals("grey")) {
					bandType = Type.GREY;
					bandLength = lineReader.nextInt();
					band = new ChromosomeBand(bandType, bandLength);
					newIdiogram.add(band);
				} else if (token.toLowerCase().equals("centromere")) {
					bandType = Type.CENTROMERE;
					bandLength = lineReader.nextInt();
					band = new ChromosomeBand(bandType, bandLength);
					newIdiogram.add(band);
				} else if (token.toLowerCase().equals("chromosome")) {
					chromosomeNumber = lineReader.nextInt();
					resolution = lineReader.nextInt();
					newIdiogram = new Idiogram();
					newIdiogram.setChromosomeNumber(chromosomeNumber);
				}
			}// End while(lineReader.hasNext())

			if (newIdiogram.getBands().size() > 0) {
				idiogramMap.put(chromosomeNumber, newIdiogram);
			}

		}// End while(fileReader.hasNext())
	}// End idiogramSheetParser

	/**
	 * Gets the chromosome number that the requested idiogram maps to. If the idiogram requested is
	 * not in the list of keys then null is returned.
	 * 
	 * @param i
	 *            Requested idiogram
	 * @return Chromosome number mapped to or null if the requested idiogram doesn't exist.
	 */
	public Idiogram get(int index) {
		return idiogramMap.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String idiogramMapString = "";

		Set<Integer> idiograms = idiogramMap.keySet();

		for (Integer i : idiograms) {
			idiogramMapString += "Chromosome number " + i + ": " + idiogramMap.get(i).toString()
					+ "\n";
		}

		return idiogramMapString;
	}

}
