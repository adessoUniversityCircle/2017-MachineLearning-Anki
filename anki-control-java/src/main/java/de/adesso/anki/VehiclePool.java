package de.adesso.anki;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class VehiclePool implements Iterable<Vehicle>, AutoCloseable {

  private final VehicleGateway vg;
  private Map<String, Vehicle> vehicles;
  
  public VehiclePool(VehicleGateway vg) {
    this.vg = Objects.requireNonNull(vg, "gateway must not be null");
    this.vehicles = new TreeMap<>();
    scanVehicles();
  }

  public void scanVehicles() {
    for (Vehicle v : vehicles.values()) {
      saveDisconnect(v);
    }

    for (Vehicle v : vg.findVehicles()) {
      if (!v.toString().equalsIgnoreCase("NUKE A0")) {
        vehicles.put(v.toString().split(" ")[1].toLowerCase(), v);
      } else {
        vehicles.put("A1".toLowerCase(), v);
      }
    }
  }

  public int size() {
    return vehicles.size();
  }

  public boolean checkId(String idx) {
    return vehicles.containsKey(idx);
  }

  public Vehicle get(String idx) {
    if (!checkId(idx))
      return null;

    return vehicles.get(idx);
  }

  @Override
  public void close() {
    try {
      for (Vehicle v : vehicles.values()) {
        saveDisconnect(v);
      }
    } finally {
      vg.close();
    }
  }

  public void saveDisconnect(Vehicle v) {
    if (v.isConnected()) {
      try {
        v.stop();
        Thread.sleep(250);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        v.disconnect();
      }
    }
  }

  @Override
  public Iterator<Vehicle> iterator() {
    return vehicles.values().iterator();
  }

}
