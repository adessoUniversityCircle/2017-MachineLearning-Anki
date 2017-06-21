package de.adesso.anki.console.learning;

import de.adesso.anki.learning.State;

public class AnkiState implements State {

	private final int segment;

	public AnkiState(int segment) {
		this.segment = segment;
	}

	public int getSegment() {
		return segment;
	}

	@Override
	public boolean isFinal() {
		return segment == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + segment;
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
		AnkiState other = (AnkiState) obj;
		if (segment != other.segment)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + segment + "]";
	}

}
