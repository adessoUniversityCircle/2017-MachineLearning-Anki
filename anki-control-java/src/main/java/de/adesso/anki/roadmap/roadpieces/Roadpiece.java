package de.adesso.anki.roadmap.roadpieces;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.ImmutableList;

import de.adesso.anki.roadmap.Roadmap;
import de.adesso.anki.roadmap.segments.Segment;
import de.adesso.anki.util.Position;

public class Roadpiece {
  private final static Reflections reflections = new Reflections("de.adesso.anki.roadmap.roadpieces");
  
  private Position position;
  protected Segment segment;
  
  private int roadpieceId;
  
  public static Roadpiece createFromId(int roadpieceId) {
    Set<Class<? extends Roadpiece>> roadpieces = reflections.getSubTypesOf(Roadpiece.class);

    for (Class<? extends Roadpiece> roadpiece : roadpieces) {
      try {
        int[] ids = (int[]) roadpiece.getField("ROADPIECE_IDS").get(null);

        if (Arrays.binarySearch(ids, roadpieceId) >= 0) {
          Roadpiece piece = roadpiece.newInstance();
          piece.roadpieceId = roadpieceId;
          return piece;
        }
      } catch (NoSuchFieldException | IllegalAccessException | InstantiationException | ClassCastException e) {
        // just skip the Roadpiece subclass if there is no ROADPIECE_IDS constant
        // or it cannot be instantiated
      }
    }

    return null;
  }
  
  public Position getPosition() {
    return position;
  }
  
  public void setPosition(Position position) {
    this.position = position;
  }

  public List<Segment> getSegments() {
    return ImmutableList.of(segment);
  }
  
  public String getType() {
    return getClass().getSimpleName();
  };
  
  public Segment getSegmentByLocation(int locationId, boolean reverse)
  {
    return reverse ? segment.reverse() : segment;
  }
  
  public Position mapOffsetToPosition(Segment segment, Position offset) {
    return segment.getEntry().transform(offset);
  }
  
  
  public static void main(String[] args) {
    
    Roadmap map = new Roadmap();

    map.add(18, 0, true);
    map.add(20, 0, true);
    map.add(36, 0, true);
    map.add(10, 0, false);
    map.add(39, 0, true);
    map.add(17, 0, false);
    map.add(23, 0, false);
    map.add(40, 0, false);
    map.add(17, 0, false);
    map.add(10, 0, false);
    map.add(18, 0, true);
    map.add(33, 0, true);
    map.add(34, 0, true);
    
    System.out.println(map.toString());
    
    System.exit(0);
  }

  public int getId() {
    return roadpieceId;
  }
  
  public double getLength(Segment segment, double offset) {
    return segment.getExit().distance(segment.getEntry()); 
  }

  public double getOffsetByLocation(int locationId) {
    // TODO Auto-generated method stub
    return 0;
  }

}
