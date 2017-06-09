package de.adesso.anki.roadmap.roadpieces;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.adesso.anki.roadmap.segments.Segment;
import de.adesso.anki.util.Position;

public class IntersectionRoadpiece extends Roadpiece {

  public final static int[] ROADPIECE_IDS = { 10 };
  
  public final static Position NORTH = Position.at(0, -280, -90);
  public final static Position SOUTH = Position.at(0, 280, -90);
  public final static Position WEST = Position.at(-280, 0);
  public final static Position EAST = Position.at(280, 0);
  
  private Segment sectionH;
  private Segment sectionV;

  public IntersectionRoadpiece() {
    this.sectionH = new Segment(this, WEST, EAST);
    this.sectionV = new Segment(this, NORTH, SOUTH);
  }
  
  @Override
  public List<Segment> getSegments() {
    return ImmutableList.of(sectionH, sectionV);
  }
  
  @Override
  public Segment getSegmentByLocation(int locationId, boolean reverse) {
    switch (locationId / 4) {
      case 0: return reverse ? sectionH.reverse() : sectionH;
      case 1: return reverse ? sectionV : sectionV.reverse();
      case 2: return reverse ? sectionH : sectionH.reverse();
      case 3: return reverse ? sectionV.reverse() : sectionV;
      default: return null;
    }
  }
  

}
