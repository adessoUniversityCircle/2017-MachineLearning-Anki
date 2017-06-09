package de.adesso.anki.roadmap.segments;

import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import de.adesso.anki.util.Position;

public class ReverseSegment extends Segment {

  private Segment original;
  
  public ReverseSegment(Segment original) {
    this.original = original;
  }
  
  public Segment reverse() {
    return original;
  }

  public Segment getPrev() {
    return original.getNext() != null ? original.getNext().reverse() : null;
  }
  
  public void setPrev(Segment prev) {
    original.setNext(prev.reverse());
  }

  public Segment getNext() {
    return original.getPrev() != null ? original.getPrev().reverse() : null;
  }

  public void setNext(Segment next) {
    original.setPrev(next.reverse());
  }

  public Roadpiece getPiece() {
    return original.getPiece();
  }

  public Position getEntry() {
    return original.getExit().reverse();
  }

  public Position getExit() {
    return original.getEntry().reverse();
  }
  
  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null)
      return false;
    if (this.getClass() != other.getClass())
      return false;
    
    return this.original == ((ReverseSegment) other).original;
  }
  
  @Override
  public double getOffsetByLocation(int locationId) {
    return -1 * original.getOffsetByLocation(locationId);
  }
  
  @Override
  public String toString() {
    return original.toString() + " reversed";
  }

}
