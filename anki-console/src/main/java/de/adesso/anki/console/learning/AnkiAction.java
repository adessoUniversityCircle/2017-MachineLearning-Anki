package de.adesso.anki.console.learning;

import de.adesso.anki.learning.Action;

public class AnkiAction implements Action {

	public static final AnkiAction DEFAULT_ACTION = new AnkiAction(1000, -13.5);

	private final int speed;
	private final double offset;

	public AnkiAction(int speed, double offset) {
		this.speed = speed;
		this.offset = offset;
	}

	public int getSpeed() {
		return speed;
	}

	public double getOffset() {
		return offset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(offset);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + speed;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnkiAction other = (AnkiAction) obj;
		if (Double.doubleToLongBits(offset) != Double.doubleToLongBits(other.offset))
			return false;
		if (speed != other.speed)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + speed + "," + offset + "]";
	}

}
