package de.adesso.anki.roadmap.roadpieces;

import de.adesso.anki.roadmap.segments.Segment;
import de.adesso.anki.util.Position;

public class StraightRoadpiece extends Roadpiece {
  
  public final static int[] ROADPIECE_IDS = { 36, 39, 40, 48, 51 };
  public final static Position ENTRY = Position.at(-280, 0);
  public final static Position EXIT = Position.at(280, 0);

  public StraightRoadpiece() {
    this.segment = new Segment(this, ENTRY, EXIT);
  }
  
  @Override
  public double getOffsetByLocation(int locationId) {
    return 9.0*(locationId/3) - 67.5;
  }

}
