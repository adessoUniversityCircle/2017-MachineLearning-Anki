package de.adesso.anki.roadmap;

import de.adesso.anki.Vehicle;
import de.adesso.anki.sdk.messages.LocalizationPositionUpdateMessage;
import de.adesso.anki.sdk.messages.LocalizationTransitionUpdateMessage;
import de.adesso.anki.sdk.messages.MessageListener;

public class RoadmapScanner {

  private Vehicle vehicle;
  private Roadmap roadmap;
 
  private MessageListener<LocalizationPositionUpdateMessage> positionUpdateListener;
  private MessageListener<LocalizationTransitionUpdateMessage> transitionUpdateListener;
  
  private LocalizationPositionUpdateMessage lastPosition;
  
  public RoadmapScanner(Vehicle vehicle) {
    this.vehicle = vehicle;
    this.roadmap = new Roadmap();
  }
  
  public void startScanning() {
    positionUpdateListener = (message) -> handlePositionUpdate(message);
    transitionUpdateListener = (message) -> handleTransitionUpdate(message);
    vehicle.addMessageListener(LocalizationPositionUpdateMessage.class, positionUpdateListener);    
    vehicle.addMessageListener(LocalizationTransitionUpdateMessage.class, transitionUpdateListener);    
    vehicle.setDesiredSpeed(500);
  }
  
  public void stopScanning() {
    vehicle.setDesiredSpeed(0);
    vehicle.removeMessageListener(LocalizationPositionUpdateMessage.class, positionUpdateListener);    
    vehicle.removeMessageListener(LocalizationTransitionUpdateMessage.class, transitionUpdateListener); 
  }
  
  public boolean isComplete() {
    return roadmap.isComplete();
  }
  
  public Roadmap getRoadmap() {
    return roadmap;
  }

  private void handlePositionUpdate(LocalizationPositionUpdateMessage message) {
    lastPosition = message;
  }

  private void handleTransitionUpdate(LocalizationTransitionUpdateMessage message) {
    if (lastPosition != null) {
      roadmap.add(
          lastPosition.getRoadPieceId(),
          lastPosition.getLocationId(),
          lastPosition.isParsedReverse()
      );
      
      if (roadmap.isComplete()) {
        this.stopScanning();
      }
    }
  }
  

}
