package de.adesso.anki.sdk.messages;

import com.google.common.base.MoreObjects;

/**
 * Notifies the controller that the vehicle received a PingRequestMessage.
 *
 * @author Yannick Eckey <yannick.eckey@adesso.de>
 */
public class PingResponseMessage extends Message {
  public static final int TYPE = 0x17;
  
  public PingResponseMessage() {
    this.type = TYPE;
  }
  
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .toString();
  }
}
