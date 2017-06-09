package de.adesso.anki.roadmap.roadpieces;

import de.adesso.anki.roadmap.segments.Segment;
import de.adesso.anki.util.Position;

public class FinishRoadpiece extends Roadpiece {

  public final static int[] ROADPIECE_IDS = { 34 };
  public final static Position ENTRY = Position.at(-170, 0);
  public final static Position EXIT = Position.at(170, 0);

  public FinishRoadpiece() {
    this.segment = new Segment(this, ENTRY, EXIT);
  }
  
  @Override
  public double getOffsetByLocation(int locationId) {
    return 9.0*(locationId/2) - 67.5;
  }
  

}
