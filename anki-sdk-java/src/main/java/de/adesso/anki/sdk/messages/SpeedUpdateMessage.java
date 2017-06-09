package de.adesso.anki.sdk.messages;

import java.nio.ByteBuffer;

import com.google.common.base.MoreObjects;

/**
 * Notifies the controller that the vehicle deviated from its desired driving lane.
 * 
 * @author Yannick Eckey <yannick.eckey@adesso.de>
 */
public class SpeedUpdateMessage extends Message {
  public static final int TYPE = 0x36;
  
  private int desiredSpeed; // unsigned short
  private int desiredAcceleration; // unsigned short
  private int lastKnownSpeed; // unsigned short
  
  public SpeedUpdateMessage() {
    this.type = TYPE;
  }
  
  @Override
  protected void parsePayload(ByteBuffer buffer) {
    this.desiredSpeed = Short.toUnsignedInt(buffer.getShort());
    this.desiredAcceleration = Short.toUnsignedInt(buffer.getShort());
    this.lastKnownSpeed = Short.toUnsignedInt(buffer.getShort());
  }
  
  @Override
  protected void preparePayload(ByteBuffer buffer) {
    
  }
  
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("speed", desiredSpeed)
        .add("accel", desiredAcceleration)
        .add("lastSpeed", lastKnownSpeed)
        .toString();
  }

  public int getDesiredSpeed() {
    return desiredSpeed;
  }

  public int getDesiredAcceleration() {
    return desiredAcceleration;
  }

  public int getLastKnownSpeed() {
    return lastKnownSpeed;
  }
}
