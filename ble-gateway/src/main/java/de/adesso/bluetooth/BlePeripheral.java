package de.adesso.bluetooth;

import de.adesso.util.NotificationListener;

public class BlePeripheral {

  private BleGateway gateway;
  private final String address;
  private final String manufacturerData;
  private final String localName;
  
  private boolean connected;
  
  public void connect() {
    try {
      int count = 0;
      int maxTries = 20;
      connected = false;
      
      // subscribe((data) -> { connected = true; });
      
      while (!connected) {
        try {
          gateway.connect(this);
          connected = true;
          //sendMessage(new PingRequestMessage());
          Thread.sleep(1000);
        } catch (RuntimeException e) {
          connected = false;
          if (++count == maxTries)
            throw e;
        }
      }
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
    
  void setGateway(BleGateway gateway) {
    this.gateway = gateway;
  }
  
  public void subscribe(NotificationListener listener) {
    gateway.subscribe(this, listener);
  }

  public void disconnect() {
    gateway.disconnect(this);
  }
  
  public void send(String data) {
    gateway.send(this, data);
  }
  
  public BlePeripheral(BleGateway gateway, String address, String manufacturerData, String localName) {
    this.gateway = gateway;
    this.address = address;
    this.manufacturerData = manufacturerData;
    this.localName = localName;
  }
  
  public String getAddress() {
    return address;
  }

  public String getManufacturerData() {
    return manufacturerData;
  }

  public String getLocalName() {
    return localName;
  }

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((address == null) ? 0 : address.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	BlePeripheral other = (BlePeripheral) obj;
	if (address == null) {
		if (other.address != null)
			return false;
	} else if (!address.equals(other.address))
		return false;
	return true;
}

}
