package de.adesso.anki.sdk;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import de.adesso.bluetooth.BleGateway;
import de.adesso.bluetooth.BleMultiGateway;

public class AnkiGateway {
  
  private String host;
  private int port;
  
  private BleGateway gateway;
  
  public AnkiGateway(String host, int port) throws IOException {
    this.host = host;
    this.port = port;
    
    this.gateway = new BleGateway(host, port);
  }
  
  public AnkiGateway(String... hosts) {
	  this.gateway = new BleMultiGateway(hosts);
  }
  
  public List<AnkiVehicle> findVehicles() {
    return gateway.findPeripherals().stream()
        .map(p -> new AnkiVehicle(p))
        .collect(Collectors.toList());
  }

  public void close() {
    gateway.close();
  }

}
