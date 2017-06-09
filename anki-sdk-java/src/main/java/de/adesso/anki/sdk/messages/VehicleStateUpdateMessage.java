package de.adesso.anki.sdk.messages;

import java.nio.ByteBuffer;

import com.google.common.base.MoreObjects;

/**
 * Notifies the controller about the vehicle's current battery level.
 * Can be requested by sending BatteryLevelRequestMessage.
 * 
 * @author Yannick Eckey <yannick.eckey@adesso.de>
 */
public class VehicleStateUpdateMessage extends Message {
  public static final int TYPE = 0x3f;

  private boolean onTrack; // unsigned byte
  private boolean charging; // unsigned byte
  private boolean lowBattery; // unsigned byte
  private boolean fullBattery; // unsigned byte

  public VehicleStateUpdateMessage() {
    this.type = TYPE;
  }

  @Override
  public void parsePayload(ByteBuffer buffer) {
    this.onTrack = buffer.get() > 0;
    this.charging = buffer.get() > 0;
    this.lowBattery = buffer.get() > 0;
    this.fullBattery = buffer.get() > 0;
  }

  @Override
  protected void preparePayload(ByteBuffer buffer) {
    
  }
  
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("charging", charging)
        .add("onTrack", onTrack)
        .add("lowBattery", lowBattery)
        .add("fullBattery", fullBattery)
        .toString();
  }
  
  public boolean isOnTrack() {
    return onTrack;
  }
  
  public boolean isCharging() {
    return charging;
  }
  
  public boolean isBatteryLow() {
    return lowBattery;
  }
  
  public boolean isBatteryFull() {
    return fullBattery;
  }
}
