package de.adesso.anki.roadmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.adesso.anki.roadmap.roadpieces.IntersectionRoadpiece;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import de.adesso.anki.roadmap.segments.ReverseSegment;
import de.adesso.anki.roadmap.segments.Segment;
import de.adesso.anki.util.Position;

public class Roadmap {
  
  private Segment anchor;
  private Segment current;
  
  public void setAnchor(Segment anchor) {
    this.anchor = anchor;
  }
  
  public void addSegment(Segment segment) {
    if (current == null) {
      anchor = current = segment;
      anchor.getPiece().setPosition(Position.at(0,0,0));
    }
    else {
      current.connect(segment);
      current = segment;

      Position currentExit = current.getExitPosition();
      Position anchorEntry = anchor.getEntryPosition();
      if (currentExit.distance(anchorEntry) < 1) {
        current.connect(anchor);
      }
    }
  }
  
  public void add(int roadpieceId, int locationId, boolean reverse) {
    Roadpiece piece = Roadpiece.createFromId(roadpieceId);
    if (piece instanceof IntersectionRoadpiece)
      piece = findRoadpieceById(10) != null ? findRoadpieceById(10) : piece;
      
    Segment segment = piece.getSegmentByLocation(locationId, reverse);
    
    this.addSegment(segment);
  }
  
  public List<Roadpiece> toList() {
    if (anchor == null) return Collections.emptyList();
    
    List<Roadpiece> list = new ArrayList<>();
    list.add(anchor.getPiece());
    
    Segment iterator = anchor.getNext();
    while (iterator != null && !iterator.equals(anchor)) {
      if (iterator.getPiece() != null && !list.contains(iterator.getPiece())) {
        list.add(iterator.getPiece());
      }
      iterator = iterator.getNext();
    }
    
    return Collections.unmodifiableList(list);
  }
  
  public boolean isComplete() {
    return anchor != null && anchor.getPrev() != null;
  }

  public Roadpiece findRoadpieceById(int roadPieceId) {
    if (anchor == null)
      return null;
    
    if (anchor.getPiece().getId() == roadPieceId)
      return anchor.getPiece();
    
    Segment iterator = anchor.getNext();
    
    while (iterator != null) {
      if (iterator.getPiece().getId() == roadPieceId)
        return iterator.getPiece();
      
      if (iterator.equals(anchor)) return null;
      
      iterator = iterator.getNext();
    }
    
    return null;
  }
  
  public String toString() {
    if (anchor == null)
      return "Roadmap: empty";
    
    StringBuilder sb = new StringBuilder();
    sb.append("Roadmap: ");
    
    Segment iterator = anchor;
    while (iterator != null) {
      sb.append(iterator.getPiece().getId());
      if (iterator instanceof ReverseSegment)
        sb.append("R");
      
      iterator = iterator.getNext();
      if (iterator != null)
        sb.append(" -> ");
      if (iterator.equals(anchor)) {
        sb.append("COMPLETE");
        iterator = null;
      }
    }
    
    return sb.toString();
  }
  

  public Position getPosition() {
    // TODO Auto-generated method stub
    return null;
  }
  
  public double getWidth() {
    // TODO Auto-generated method stub
    return 0;
  }
  
  public double getHeight() {
    // TODO Auto-generated method stub
    return 0;
  }
  
  public Set<Roadpiece> getRoadpieces() {
    // TODO Auto-generated method stub
    return null;
  }

}
