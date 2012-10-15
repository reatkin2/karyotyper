package basic_objects;

import java.util.Collection;
import java.util.LinkedList;

public class Ideogram {
	private LinkedList<ChromosomeBand> myBands;
	private int length;
	
	public Ideogram() {
		this.myBands = new LinkedList<ChromosomeBand>();
		this.length = 0;
	}
	
	public Ideogram(Ideogram i) {
		this.length = i.length();
		this.myBands = i.getBands();
	}
	
	public Ideogram(Collection<ChromosomeBand> c) {
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
}