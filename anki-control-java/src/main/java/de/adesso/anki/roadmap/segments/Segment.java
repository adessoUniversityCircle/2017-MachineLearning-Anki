package de.adesso.anki.roadmap.segments;

import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import de.adesso.anki.util.Position;

public class Segment {
  private Roadpiece piece;
  
  private Position entry;
  
  protected Segment() { }
  
  public Segment(Roadpiece piece, Position entry, Position exit) {
    this.piece = piece;
    this.entry = entry;
    this.exit = exit;
  }
  
  public Segment getPrev() {
    return prev;
  }

  public void setPrev(Segment prev) {
    this.prev = prev;
  }

  public Segment getNext() {
    return next;
  }

  public void setNext(Segment next) {
    this.next = next;
  }

  public Roadpiece getPiece() {
    return piece;
  }

  public Position getEntry() {
    return entry;
  }

  public Position getExit() {
    return exit;
  }

  private Position exit;
  
  private Segment prev;
  private Segment next;
  
  public void connect(Segment other) {
    this.setNext(other);
    other.setPrev(this);
    
    Position otherPos = this.getExitPosition().invTransform(other.getEntry());
    other.getPiece().setPosition(otherPos);
  }
  
  public Segment reverse() {
    return new ReverseSegment(this);
  }
  
  public Position getPosition() {
    return this.getPiece().getPosition();
  }

  public Position getEntryPosition() {
    return this.getPosition().transform(this.getEntry());
  }

  public Position getExitPosition() {
    return this.getPosition().transform(this.getExit());
  }
  
  public Position mapOffsetToPosition(Position offset) {
    return getPiece().mapOffsetToPosition(this, offset);
  }

  public double getOffsetByLocation(int locationId) {
    return getPiece().getOffsetByLocation(locationId);
  }
  
  public double getLength(double offset) {
    return getPiece().getLength(this, offset);
  }
  
  @Override
  public String toString() {
    return getPiece().getClass().getSimpleName() + "(" + getPiece().getId() + ")";
  }

}
