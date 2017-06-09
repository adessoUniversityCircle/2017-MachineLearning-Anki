package de.adesso.anki.sdk.messages;

import javax.xml.bind.DatatypeConverter;

import com.google.common.base.MoreObjects;

/**
 * Requests the vehicle to send its current battery level.
 * Vehicle will respond with BatteryLevelResponseMessage.
 * 
 * @author Yannick Eckey <yannick.eckey@adesso.de>
 */
public class BatteryLevelRequestMessage extends Message {
  public static final int TYPE = 0x1a;
  
  public BatteryLevelRequestMessage() {
    this.type = TYPE;
  }
  
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .toString();
  }
}
