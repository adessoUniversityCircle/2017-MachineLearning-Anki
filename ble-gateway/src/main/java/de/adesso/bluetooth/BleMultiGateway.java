package de.adesso.bluetooth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import de.adesso.util.NotificationListener;

public class BleMultiGateway extends BleGateway {
  
  List<BleGateway> gateways;
  
  Map<BlePeripheral, BleGateway> mapping;

  public BleMultiGateway(String... hosts) {
    gateways = Lists.newArrayList(hosts).stream().map( host -> {
      try {
        return new BleGateway(host);
      } catch (IOException e) {
        return null;
      }
    } ).filter(x -> x != null).collect(Collectors.toList());
    
    mapping = new HashMap<>();
  }

  @Override
  public List<BlePeripheral> findPeripherals() {
    List<BlePeripheral> cars = new ArrayList<>(gateways.stream().parallel().flatMap(x -> x.findPeripherals().stream()).collect(Collectors.toSet()));
    cars.forEach(x -> x.setGateway(this));
    return cars;
  }

  @Override
  public void connect(BlePeripheral peripheral) throws InterruptedException {
    Optional<BleGateway> best = gateways.stream().min(Comparator.comparingInt(x -> x.getConnectionCount()));
    if (best.isPresent()) {
      best.get().connect(peripheral);
      mapping.put(peripheral, best.get());
    }
  }

  @Override
  public void send(BlePeripheral vehicle, String data) {
    mapping.get(vehicle).send(vehicle, data);
  }

  @Override
  public void subscribe(BlePeripheral vehicle, NotificationListener listener) {
    mapping.get(vehicle).subscribe(vehicle, listener);
  }

  @Override
  public void unsubscribe(BlePeripheral vehicle, NotificationListener listener) {
    mapping.get(vehicle).unsubscribe(vehicle, listener);
  }

  @Override
  public void fireDataReceived(BlePeripheral peripheral, String data) {
    mapping.get(peripheral).fireDataReceived(peripheral, data);
  }

  @Override
  public void disconnect(BlePeripheral vehicle) {
    mapping.get(vehicle).disconnect(vehicle);
    mapping.remove(vehicle);
  }
  
  @Override
  public int getConnectionCount() {
    return gateways.stream().mapToInt(x -> x.getConnectionCount()).sum();
  }

  @Override
  public void close() {
    gateways.forEach(x -> x.close());
  }
  
  
  
}
