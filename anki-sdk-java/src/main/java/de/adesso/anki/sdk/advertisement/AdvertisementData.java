package de.adesso.anki.sdk.advertisement;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.xml.bind.DatatypeConverter;

import de.adesso.anki.sdk.messages.VehicleStateUpdateMessage;

public class AdvertisementData {

  private int identifier;
  private int modelId;
  private int productId;
  private int _reserved;

  private int state;
  private int version;
  
  private boolean charging;
  private boolean onTrack;
  private boolean lowBattery;
  private boolean fullBattery;

  public AdvertisementData(String manufacturerData, String localName) {

    byte[] data = DatatypeConverter.parseHexBinary(manufacturerData);
    ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

    productId = buffer.getShort();
    _reserved = buffer.get();
    modelId = buffer.get();
    identifier = buffer.getInt();

    byte[] name = DatatypeConverter.parseHexBinary(localName);
    buffer = ByteBuffer.wrap(name).order(ByteOrder.LITTLE_ENDIAN);

    state = buffer.get();
    version = buffer.getShort();
    
    onTrack = (state & 0x80) == 0x80;
    charging = (state & 0x40) == 0x40;
    fullBattery = (state & 0x10) == 0x10;
    lowBattery = (state & 0x20) == 0x20;
  }

  public int getIdentifier() {
    return identifier;
  }

  public int getModelId() {
    return modelId;
  }

  public int getProductId() {
    return productId;
  }
  
  public int getVersion() {
    return version;
  }

  public AnkiModel getModel() {
    return AnkiModel.fromId(modelId);
  }

  public boolean isCharging() {
    return charging;
  }
  
  public int getBatteryState() {
    if (fullBattery)
      return 100;
    if (lowBattery)
      return 10;
    
    return 60;
  }
  
  public void updateState(VehicleStateUpdateMessage update) {
    this.charging = update.isCharging();
    this.lowBattery = update.isBatteryLow();
    this.fullBattery = update.isBatteryFull();
    this.onTrack = update.isOnTrack();
  }

  public String toString() {
    return String.format("%s %X", getModel(), Integer.divideUnsigned(getIdentifier(), 0x1000000));
  }

}
