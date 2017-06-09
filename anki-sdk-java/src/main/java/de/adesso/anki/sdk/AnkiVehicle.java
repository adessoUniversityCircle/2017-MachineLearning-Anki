package de.adesso.anki.sdk;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import de.adesso.anki.sdk.advertisement.AdvertisementData;
import de.adesso.anki.sdk.messages.IMessage;
import de.adesso.anki.sdk.messages.Message;
import de.adesso.anki.sdk.messages.MessageListener;
import de.adesso.bluetooth.BlePeripheral;

public class AnkiVehicle {
  
  private BlePeripheral peripheral;
  
  private Multimap<Class<? extends IMessage>, MessageListener> listeners;
  private AdvertisementData advertisement;

  public AnkiVehicle(BlePeripheral peripheral) {
    this.listeners = LinkedListMultimap.create();
    this.peripheral = peripheral;
    this.advertisement = new AdvertisementData(peripheral.getManufacturerData(), peripheral.getLocalName());
  }
  
  public void sendMessage(Message message) {
    peripheral.send(message.toHex());
    //System.out.println(String.format("[%s] > %s: %s", LocalTime.now(), this, message));
  }
  
  @Deprecated
  public void addMessageListener(MessageListener listener) {
    this.addMessageListener(Message.class, listener);
  }
  
  @Deprecated
  public void removeMessageListener(MessageListener listener) {
    this.removeMessageListener(Message.class, listener);
  }
  
  public <T extends IMessage> MessageListener<T> addMessageListener(Class<T> klass, MessageListener<T> listener) {
    this.listeners.put(klass, listener);
    return listener;
  }
  
  public <T extends IMessage> void removeMessageListener(Class<T> klass, MessageListener<T> listener) {
    this.listeners.remove(klass, listener);
  }
  
  public void removeAllListeners() {
    this.listeners.clear();
  }
  
  public <T extends IMessage> void fireMessageReceived(T message) {
    for (MessageListener<T> l : this.listeners.get(Message.class)) {
      l.messageReceived(message);
    }
    if (message.getClass() != Message.class) {
      for (MessageListener<T> l : this.listeners.get(message.getClass())) {
        l.messageReceived(message);
      }
    }
  }

  public AdvertisementData getAdvertisement() {
    return advertisement;
  }
  
  public String getAddress() {
    return peripheral.getAddress();
  }

  public String toString() {
    return advertisement.toString();
  }

  public String getColor() {
    return advertisement.getModel().getColor();
  }
  
  public void connect() {
    peripheral.connect();
    
    this.peripheral.subscribe((data) -> {
      String[] parts = data.split(";");
      Message message = Message.parse(parts[0], Long.parseLong(parts[1]));
      fireMessageReceived(message);
    });
  }
  
  public void disconnect() {
    peripheral.disconnect();
    removeAllListeners();
  }

  public boolean getCurrentPiece() {
    return false;
  }

}
