package de.adesso.anki.roadmap.roadpieces;

import de.adesso.anki.roadmap.segments.Segment;
import de.adesso.anki.util.Position;

public class StartRoadpiece extends Roadpiece {
  
  public final static int[] ROADPIECE_IDS = { 33 };
  public final static Position ENTRY = Position.at(-110, 0);
  public final static Position EXIT = Position.at(110, 0);

  public StartRoadpiece() {
    this.segment = new Segment(this, ENTRY, EXIT);
  }
  
  @Override
  public double getOffsetByLocation(int locationId) {
    return 9.0*locationId - 67.5;
  }

}
