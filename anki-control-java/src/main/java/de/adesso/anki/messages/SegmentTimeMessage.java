package de.adesso.anki.messages;

import de.adesso.anki.roadmap.segments.SegmentType;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class SegmentTimeMessage implements VehicleMessage {

  protected final String vehicle;
  protected final int segmentPosInMap;
  protected final SegmentType type;
  protected final long lastMeasure;
  protected final long timestamp;

  public SegmentTimeMessage(String vehicle, int segmentPosInMap, SegmentType type, long lastMeasure, long timestamp) {
    this.vehicle = vehicle;
    this.segmentPosInMap = segmentPosInMap;
    this.type = type;
    this.lastMeasure = lastMeasure;
    this.timestamp = timestamp;
  }

  public String getVehicle() {
    return vehicle;
  }

  public int getSegmentPosInMap() {
    return segmentPosInMap;
  }

  public SegmentType getType() {
    return type;
  }

  public long getLastMeasure() {
    return lastMeasure;
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "MeasureMessage [vehicle=" + vehicle + ", segmentPosInMap=" + segmentPosInMap + ", type=" + type + ", lastMeasure=" + lastMeasure
        + ", timestamp=" + timestamp + "]";
  }

}
