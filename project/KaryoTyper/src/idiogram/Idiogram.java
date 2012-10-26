package idiogram;

import java.util.Collection;
import java.util.LinkedList;

import characterization.ChromosomeBand;

import basic_objects.Chromosome;

public class Idiogram {
	private LinkedList<ChromosomeBand> myBands;
	private int length;
	private int chromosomeNumber;
	private int resolution;

	public Idiogram() {
		this.myBands = new LinkedList<ChromosomeBand>();
		this.length = 0;
		this.chromosomeNumber = 0;
	}
	
	public Idiogram(Idiogram i) {
		this.length = i.length();
		this.myBands = i.getBands();
		this.chromosomeNumber = i.getChromosomeNumber();
	}

	public Idiogram(Collection<ChromosomeBand> c) {
		this();
		this.addAll(c);
	}
	
	public int length() {
		return length;
	}
	
	public ChromosomeBand get(int index) {
		return this.myBands.get(index);
	}
	
	public boolean add(ChromosomeBand newBand) {
		this.length += newBand.length();
		return this.myBands.add(newBand);
	}
	
	public void add(int index, ChromosomeBand newBand) {
		this.length += newBand.length();
		this.myBands.add(index, newBand);
	}
	
	public boolean addAll(Collection<ChromosomeBand> c) {
		for (ChromosomeBand band : c) {
			this.length += band.length();
			this.myBands.add(band);
		}
		return true;
	}
	
	public void addAll(int index, Collection<ChromosomeBand> c) {
		for (ChromosomeBand band : c) {
			this.length += band.length();
			this.myBands.add(band);
		}
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<ChromosomeBand> getBands() {
		return (LinkedList<ChromosomeBand>) this.myBands.clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + length;
		result = prime * result + ((myBands == null) ? 0 : myBands.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass() || obj.getClass() != Chromosome.class)
			return false;
		Idiogram other = (Idiogram) obj;
		if (length != other.length)
			return false;
		if (myBands == null) {
			if (other.myBands != null)
				return false;
		} else if (!myBands.equals(other.myBands))
			return false;
		return true;
	}
	
	public int getChromosomeNumber() {
		return chromosomeNumber;
	}
	
	public void setChromosomeNumber(int number) {
		chromosomeNumber = number;
	}
	
	/**
	 * @return the resolution
	 */
	public int getResolution() {
		return resolution;
	}

	/**
	 * @param resolution the resolution to set
	 */
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String bandString = "";
		
		for (ChromosomeBand band : myBands) {
			bandString += "(" + band.type() + ", " + band.length() + ")";
		}
		
		return "Idiogram [myBands= [" + bandString + "], length=" + length + ", chromosomeNumber="
				+ chromosomeNumber + ", resolution=" + resolution + "]";
	}
	
	
	
}