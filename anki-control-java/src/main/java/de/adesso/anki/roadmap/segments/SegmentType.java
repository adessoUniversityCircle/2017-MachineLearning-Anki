package de.adesso.anki.roadmap.segments;

import de.adesso.anki.roadmap.roadpieces.CurvedRoadpiece;
import de.adesso.anki.roadmap.roadpieces.FinishRoadpiece;
import de.adesso.anki.roadmap.roadpieces.IntersectionRoadpiece;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import de.adesso.anki.roadmap.roadpieces.StartRoadpiece;
import de.adesso.anki.roadmap.roadpieces.StraightRoadpiece;

/**
 * 
 * @author Manuel Barbi
 *
 */
public enum SegmentType {

  STRAIGHT((byte) 0), // 000
  CURVE_LEFT((byte) 4), // 100
  CURVE_RIGHT((byte) 5), // 101
  CROSSROADS((byte) 1), // 001
  START((byte) 2), // 010
  FINISH((byte) 3); // 011

  private byte code;

  private SegmentType(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public static final SegmentType segmentToEnum(Segment seg) {
    if (seg != null) {
      Roadpiece rp = seg.getPiece();

      if (rp instanceof StraightRoadpiece) {
        return STRAIGHT;
      } else if (rp instanceof CurvedRoadpiece) {
        return (seg instanceof ReverseSegment ? CURVE_RIGHT : CURVE_LEFT);
      } else if (rp instanceof IntersectionRoadpiece) {
        return CROSSROADS;
      } else if (rp instanceof StartRoadpiece) {
        return START;
      } else if (rp instanceof FinishRoadpiece) {
        return FINISH;
      }
    }

    return null;
  }

  public static boolean isCurved(SegmentType type) {
    return type.getCode() > 3;
  }

}
