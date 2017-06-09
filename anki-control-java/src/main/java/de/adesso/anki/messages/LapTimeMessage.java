package de.adesso.anki.messages;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class LapTimeMessage implements VehicleMessage {

  protected final String vehicle;
  protected final long lapTime;
  protected final long timestamp;

  public LapTimeMessage(String vehicle, long lapTime, long timestamp) {
    this.vehicle = vehicle;
    this.lapTime = lapTime;
    this.timestamp = timestamp;
  }

  @Override
  public String getVehicle() {
    return vehicle;
  }  

  public long getLapTime() {
    return lapTime;
  }

  @Override
  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "LapTimeMessage [vehicle=" + vehicle + ", lapTime=" + lapTime + ", timestamp=" + timestamp + "]";
  }

}
