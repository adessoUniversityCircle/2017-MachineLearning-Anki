package de.adesso.anki;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import de.adesso.anki.sdk.AnkiGateway;

public class VehicleGateway {
  
  private AnkiGateway gateway;

  public VehicleGateway(String host, int port) throws IOException {
    this.gateway = new AnkiGateway(host, port);
  }
  
  public VehicleGateway(String... hosts) {
	  this.gateway = new AnkiGateway(hosts);
  }
  
  public List<Vehicle> findVehicles() {
    return gateway.findVehicles().stream()
        .map(v -> new Vehicle(v))
        .collect(Collectors.toList());
  }

  public void close() {
    gateway.close();
  }

}
