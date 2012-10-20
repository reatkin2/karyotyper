package basic_objects;

public class ChromosomeBand {
	public static enum Type {
		BLACK, WHITE, GREY, CENTROMERE
	};

	private final Type type;
	private final int length;
	
	public ChromosomeBand(Type type, int length) {
		if (type == this.type.CENTROMERE && length != 0) {
			throw new IllegalArgumentException("Length of a centromere band cannot be non-zero.");
		}
		
		this.type = type;
		this.length = length;
	}

	public Type type() {
		return type;
	}
	
	public int length() {
		return length;
	}
}
