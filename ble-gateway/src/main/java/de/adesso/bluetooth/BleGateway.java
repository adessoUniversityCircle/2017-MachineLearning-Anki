package de.adesso.bluetooth;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.adesso.util.NotificationListener;
import de.adesso.util.NotificationReader;

public class BleGateway {

  private Socket socket;
  
  private PrintWriter writer;
  private NotificationReader reader;

  private Multimap<BlePeripheral, NotificationListener> listeners;
  private Multimap<BlePeripheral, NotificationListener> listeners2;
  
  protected BleGateway() { }
  
  public BleGateway(String host, int port) throws IOException {
    socket = new Socket(host, port);
    writer = new PrintWriter(socket.getOutputStream(), true);
    reader = new NotificationReader(socket.getInputStream());

    listeners = ArrayListMultimap.create();
    listeners2 = ArrayListMultimap.create();
  }
  
  public BleGateway(String host) throws IOException {
    this(host, 5000);
  }
  
  public List<BlePeripheral> findPeripherals() {
    writer.println("PROTOCOL;1.1");
    writer.println("SCAN");
    List<BlePeripheral> foundPeripherals = new ArrayList<>();
    boolean expectingResponse = true;
    while (expectingResponse)
    {
      String response = reader.waitFor("SCAN;");
      if (response.equals("SCAN;COMPLETED")) {
        expectingResponse = false;
      }
      else {
        String[] parts = response.split(";");
        
        String address = parts[1];
        String manufacturerData = parts[2];
        String localName = parts[3];
        
        foundPeripherals.add(new BlePeripheral(this, address, manufacturerData, localName));
      }
    }
    return foundPeripherals;
  }

  public void connect(BlePeripheral peripheral) throws InterruptedException {
    writer.println("CONNECT;"+peripheral.getAddress());
    String response = reader.waitFor("CONNECT;");
    
    if (response.equals("CONNECT;ERROR")) {
      throw new RuntimeException("connect failed");
    }
    
    NotificationListener nl = (line) -> {
      if (line != null && line.startsWith(peripheral.getAddress())) {
        String messageString = line.replaceFirst(peripheral.getAddress()+";", "");
        fireDataReceived(peripheral, messageString);
      }
    };
    
    if (listeners2.containsKey(peripheral)) {
      for (NotificationListener l : listeners2.get(peripheral))
        reader.removeListener(l);
    }
    
    listeners2.put(peripheral, nl);
    reader.addListener(nl);
  }
  
  public void send(BlePeripheral vehicle, String data) {
    writer.println(vehicle.getAddress() + ";" + data);
    writer.flush();
  }
  
  public void subscribe(BlePeripheral vehicle, NotificationListener listener) {
    listeners.put(vehicle, listener);
  }
  
  public void unsubscribe(BlePeripheral vehicle, NotificationListener listener) {
    listeners.remove(vehicle, listener);
  }
  
  public void fireDataReceived(BlePeripheral peripheral, String data) {
    for (NotificationListener l : listeners.get(peripheral)) {
      l.onReceive(data);;
    }
  }

  public void disconnect(BlePeripheral vehicle) {
    writer.println("DISCONNECT;"+vehicle.getAddress());
    reader.waitFor("DISCONNECT;");
  }

  public void close() {
    reader.close();
    writer.close();
    
    try {
      socket.close();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  
  public int getConnectionCount() {
    writer.println("CONNECTIONS");
    return Integer.parseInt(reader.waitFor("CONNECTIONS;").replaceAll("CONNECTIONS;", ""));
  }
  
}
