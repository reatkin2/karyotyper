package basic_objects;

import java.util.HashMap;

/**
 * Wrapper for a Java HashMap that maps an idiogram to a chromosome number
 * @author Robert
 *
 */
public class IdiogramMap {
	public final int NUM_IDIOGRAMS = 69;
	
	/** Maps idiogram to chromosome number. */
	private HashMap<Idiogram, Integer> idiogramMap;
	
	/**
	 * Initializes the HashMap
	 */
	public IdiogramMap() {
		idiogramMap = new HashMap<Idiogram, Integer>(NUM_IDIOGRAMS);
		buildMap();
	}

	private void buildMap() {
		
	}
	
	/**
	 * Gets the chromosome number that the requested idiogram maps to. If the idiogram requested is
	 * not in the list of keys then null is returned.
	 * 
	 * @param i
	 *            Requested idiogram
	 * @return Chromosome number mapped to or null if the requested idiogram doesn't exist.
	 */
	public Integer get(Idiogram i) {
		return idiogramMap.get(i);
	}
}
