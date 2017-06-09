package de.adesso.anki.roadmap.roadpieces;

import de.adesso.anki.roadmap.segments.ReverseSegment;
import de.adesso.anki.roadmap.segments.Segment;
import de.adesso.anki.util.Position;

public class CurvedRoadpiece extends Roadpiece {

  public final static int[] ROADPIECE_IDS = { 17, 18, 20, 23, 24, 27 };
  public final static Position ENTRY = Position.at(-280, 0);
  public final static Position EXIT = Position.at(0, -280, 90);
  public final static Position CENTER = Position.at(-280, -280);

  public CurvedRoadpiece() {
    this.segment = new Segment(this, ENTRY, EXIT);
  }
  
  @Override
  public Position mapOffsetToPosition(Segment segment, Position offset) {
    double offsetX = offset.getX();
    double offsetY = offset.getY();
    
    double radius = segment.getEntry().transform(Position.at(0, offsetY)).distance(CENTER);
    double angleRad = offsetX / radius;
    
    double x, y, angle;
    if (segment instanceof ReverseSegment) {
      x = radius * Math.cos(angleRad) + CENTER.getX();
      y = radius * Math.sin(angleRad) + CENTER.getY();
      angle = 270 - 180 * angleRad / Math.PI;
    }
    else {
      x = radius * Math.sin(angleRad) + CENTER.getX();
      y = radius * Math.cos(angleRad) + CENTER.getY();
      angle = 180 * angleRad / Math.PI;
    }
        
    return Position.at(x, y, angle);
  }
  
  @Override
  public double getLength(Segment segment, double offset) {
    double radius = segment.getEntry().transform(Position.at(0, offset)).distance(CENTER);
    return radius * Math.PI / 2;
  }
  
  @Override
  public double getOffsetByLocation(int locationId) {
    if (locationId < 20)
      return 9.0*(locationId/2) - 67.5;
    else
      return 9.0*((locationId-20)/3) + 22.5;
  }

}
