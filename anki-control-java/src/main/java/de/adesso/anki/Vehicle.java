package de.adesso.anki;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import de.adesso.anki.messages.LapTimeMessage;
import de.adesso.anki.messages.SegmentTimeMessage;
import de.adesso.anki.roadmap.Roadmap;
import de.adesso.anki.roadmap.roadpieces.IntersectionRoadpiece;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;
import de.adesso.anki.roadmap.segments.Segment;
import de.adesso.anki.roadmap.segments.SegmentType;
import de.adesso.anki.sdk.AnkiVehicle;
import de.adesso.anki.sdk.advertisement.AnkiModel;
import de.adesso.anki.sdk.messages.BatteryLevelRequestMessage;
import de.adesso.anki.sdk.messages.BatteryLevelResponseMessage;
import de.adesso.anki.sdk.messages.ChangeLaneMessage;
import de.adesso.anki.sdk.messages.IMessage;
import de.adesso.anki.sdk.messages.LightsPatternMessage;
import de.adesso.anki.sdk.messages.LightsPatternMessage.LightChannel;
import de.adesso.anki.sdk.messages.LightsPatternMessage.LightConfig;
import de.adesso.anki.sdk.messages.LightsPatternMessage.LightEffect;
import de.adesso.anki.sdk.messages.LocalizationPositionUpdateMessage;
import de.adesso.anki.sdk.messages.LocalizationTransitionUpdateMessage;
import de.adesso.anki.sdk.messages.Message;
import de.adesso.anki.sdk.messages.MessageListener;
import de.adesso.anki.sdk.messages.ResponseListener;
import de.adesso.anki.sdk.messages.SdkModeMessage;
import de.adesso.anki.sdk.messages.SetOffsetFromRoadCenterMessage;
import de.adesso.anki.sdk.messages.SetSpeedMessage;
import de.adesso.anki.sdk.messages.TurnMessage;
import de.adesso.anki.sdk.messages.VehicleDelocalizedMessage;
import de.adesso.anki.sdk.messages.VehicleStateUpdateMessage;
import de.adesso.anki.sdk.messages.VersionRequestMessage;
import de.adesso.anki.sdk.messages.VersionResponseMessage;
import de.adesso.anki.util.Position;

public class Vehicle {

  private AnkiVehicle anki;
  private boolean connected;

  private double offsetFromCenter;
  private double desiredOffset;
  
  private int lastKnownSpeed;
  private int desiredSpeed;
  private double distanceTravelled;

  private Roadmap roadmap;
  private Segment currentSegment;
  private int segmentPosInMap = -1;

  private Position position;
  private LocalTime lastPositionRefresh;
  
  private long time = -1;
  private long lap = -1;  

  public Vehicle(AnkiVehicle anki) {
    this.anki = anki;

    this.position = Position.at(10000, 10000);
  }

  public void connect() {
    anki.connect();
    initializeVehicle();
    connected = true;

    anki.addMessageListener(LocalizationPositionUpdateMessage.class, m -> onPositionUpdate(m));
    anki.addMessageListener(LocalizationTransitionUpdateMessage.class, m -> onTransitionUpdate(m));
    anki.addMessageListener(VehicleDelocalizedMessage.class, m -> onVehicleDelocalized(m));
    anki.addMessageListener(BatteryLevelResponseMessage.class, m -> onBatteryLevelResponseMessage(m));
    anki.addMessageListener(VehicleStateUpdateMessage.class, m -> onVehicleStateUpdateMessage(m));
    
    
    LightsPatternMessage lights = new LightsPatternMessage();
    lights.add(new LightConfig(LightChannel.ENGINE_RED, LightEffect.STEADY, 0, 0, 10));
    lights.add(new LightConfig(LightChannel.ENGINE_BLUE, LightEffect.STEADY, 15, 0, 1));
    anki.sendMessage(lights);
  }

  public void disconnect() {
    anki.disconnect();
    onVehicleDelocalized(null);
    connected = false;
  }

  public void setDesiredSpeed(int speed, int acceleration) {
    desiredSpeed = speed;
    lastKnownSpeed = speed;
    Message message = new SetSpeedMessage(speed, acceleration);
    anki.sendMessage(message);
  }

  public void setDesiredSpeed(int speed) {
    setDesiredSpeed(speed, 12500);
  }

  public int getSpeed() {
    return lastKnownSpeed;
  }

  public void stop() {
    Message message = new SetSpeedMessage(0, 12500);
    anki.sendMessage(message);
  }

  public void changeLane(double offsetFromCenter, int horizontalSpeed, int horizontalAccel) {
    desiredOffset = offsetFromCenter;
    Message message =
        new ChangeLaneMessage((float) offsetFromCenter, horizontalSpeed, horizontalAccel);
    anki.sendMessage(message);
  }

  public void changeLane(double offsetFromCenter) {
    changeLane(offsetFromCenter, 250, 1000);
  }

  public void performUturn() {
    Message message = new TurnMessage();
    anki.sendMessage(message);
  }

  public String getAddress() {
    return anki.getAddress();
  }

  public int getVersion() {
    Message message = new VersionRequestMessage();
    ResponseListener<VersionResponseMessage> listener = new ResponseListener<>();
    anki.addMessageListener(VersionResponseMessage.class, listener);
    anki.sendMessage(message);

    VersionResponseMessage response = listener.awaitMessage(1000);
    return response != null ? response.getVersion() : -1;
  }


  private Date lastBatteryUpdate;
  private int batteryLevel;

  public int getBatteryLevel() {
    if (connected) {
      
      return batteryLevel;
    } else {
      return anki.getAdvertisement().getBatteryState();
    }
  }
  
  private int convertBatteryLevel(int millivolts) {
    int min = 3400;
    int max = 4200;
    
    int percentage = 100 * (millivolts - min) / (max - min); 
    
    return Math.min(Math.max(percentage, 0), 100);
  }

  public int ping() {
    Message message = new BatteryLevelRequestMessage();
    anki.sendMessage(message);
    
//    Message message = new PingRequestMessage();
//    ResponseListener<PingResponseMessage> listener = new ResponseListener<>();
//    anki.addMessageListener(PingResponseMessage.class, listener);
//
//    Date start = new Date();
//    anki.sendMessage(message);
//    PingResponseMessage response = listener.awaitMessage(1000);
//    Date end = new Date();
//    return response != null ? Math.toIntExact(end.getTime() - start.getTime()) : -1;
    return 0;
  }

  private void initializeVehicle() {
    Message sdkOn = new SdkModeMessage();
    anki.sendMessage(sdkOn);
  }

  public void setAcceleration(int acceleration) {
    // TODO Auto-generated method stub

  }

  public double getOffsetFromRoadCenter() {
    return offsetFromCenter;
  }

  public double getDesiredOffset() {
    return desiredOffset;
  }

  public Position getPosition() {
    return position;
  }

  public boolean isConnected() {
    return connected;
  }

  public boolean isCharging() {
    return anki.getAdvertisement().isCharging();
  }

  public <T extends IMessage> MessageListener<T> addMessageListener(Class<T> klass,
      MessageListener<T> listener) {
    return anki.addMessageListener(klass, listener);
  }

  public int getDesiredSpeed() {
    return desiredSpeed;
  }

  public AnkiModel getModel() {
    return anki.getAdvertisement().getModel();
  }

  public <T extends IMessage> void removeMessageListener(Class<T> klass,
      MessageListener<T> listener) {
    anki.removeMessageListener(klass, listener);
  }

  private void onPositionUpdate(LocalizationPositionUpdateMessage positionUpdate) {
    //System.out.println(LocalTime.now() + ": " + positionUpdate);
    if (roadmap != null && (currentSegment == null
        || currentSegment.getPiece().getId() != positionUpdate.getRoadPieceId())) {
      Roadpiece roadpiece = roadmap.findRoadpieceById(positionUpdate.getRoadPieceId());
      if (roadpiece != null)
        currentSegment = roadpiece.getSegmentByLocation(positionUpdate.getLocationId(),
            positionUpdate.isParsedReverse());
    }

    // update last known speed value
    if (desiredSpeed != 0)
      lastKnownSpeed = positionUpdate.getSpeed();
    lastPositionRefresh = LocalTime.now();

    if (currentSegment == null)
      return;

    // update offset from center
    int locationId = positionUpdate.getLocationId();
    double offset = currentSegment.getOffsetByLocation(locationId);

    if (offset < -0.1 || offset > 0.1) {
      offsetFromCenter = offset;

      // feedback offset to vehicle
      Message feedback = new SetOffsetFromRoadCenterMessage((float) offsetFromCenter);
      anki.sendMessage(feedback);
    }
  }

  private void onTransitionUpdate(LocalizationTransitionUpdateMessage transitionUpdate) {
    if (roadmap != null) {
      if (currentSegment == null) {
        if (lastKnownSpeed == 0 && desiredSpeed != 0)
          anki.sendMessage(new SetSpeedMessage(desiredSpeed, 12500));
        return;
      }

      // reset distance travelled
      lastPositionRefresh = LocalTime.now();
      distanceTravelled = 0;

      // update current roadpiece to next
      currentSegment = currentSegment.getNext();

      measure(transitionUpdate);
      
      // set position to entry point
      position = currentSegment.getEntryPosition();
    }
  }

  private void measure(LocalizationTransitionUpdateMessage transitionUpdate) {
    SegmentType next = SegmentType.segmentToEnum(currentSegment);
    String name = this.toString();

    if (next == SegmentType.START) {
      if (lap > -1) {
        // fire lap event
        anki.fireMessageReceived(new LapTimeMessage(name, transitionUpdate.getTimestamp() - lap, transitionUpdate.getTimestamp()));
      }

      lap = transitionUpdate.getTimestamp();
      segmentPosInMap = 0;
    }

    // fire measure event
    anki.fireMessageReceived(
        new SegmentTimeMessage(name, getSegmentPosInMap(), next, (time != -1) ? transitionUpdate.getTimestamp() - time : -1, transitionUpdate.getTimestamp()));

    time = transitionUpdate.getTimestamp();
    segmentPosInMap++;
  }

  private void onVehicleDelocalized(VehicleDelocalizedMessage message) {
    currentSegment = null;
    lastPositionRefresh = LocalTime.now();
    position = Position.at(10000, 10000);
    distanceTravelled = 0;
    lastKnownSpeed = 0;
    offsetFromCenter = 0;
  }
  
  boolean batteryEmpty;
  private void onBatteryLevelResponseMessage(BatteryLevelResponseMessage message) {
    batteryLevel = convertBatteryLevel(message.getBatteryLevel());
    lastBatteryUpdate = new Date();
    
    if (batteryLevel < 15) {
      LightsPatternMessage lights = new LightsPatternMessage();
      lights.add(new LightConfig(LightChannel.ENGINE_RED, LightEffect.THROB, 15, 0, 10));
      lights.add(new LightConfig(LightChannel.ENGINE_BLUE, LightEffect.STEADY, 0, 0, 1));
      anki.sendMessage(lights);
      
      batteryEmpty = true;
    }
  }

  private void onVehicleStateUpdateMessage(VehicleStateUpdateMessage message) {
    this.anki.getAdvertisement().updateState(message);
    
    if (message.isCharging() || !message.isOnTrack()) {
      onVehicleDelocalized(null);
    }
    
    if (message.isCharging()) {
      LightsPatternMessage lights = new LightsPatternMessage();
      lights.add(new LightConfig(LightChannel.ENGINE_RED, LightEffect.THROB, 0, 0, 10));
      lights.add(new LightConfig(LightChannel.ENGINE_BLUE, LightEffect.STEADY, 15, 0, 1));
      anki.sendMessage(lights);
      stop();
      
      batteryEmpty = false;
    }
  }

  private void updateDistanceTravelled() {
    if (lastPositionRefresh == null)
      return;
    // calculate time since last update
    LocalTime now = LocalTime.now();
    double deltaT = (lastPositionRefresh.until(now, ChronoUnit.MILLIS)) / 1000.0;
    lastPositionRefresh = now;

    // calculate distance travelled since last update
    double deltaX = deltaT * lastKnownSpeed;

    // increment distance by result
    distanceTravelled += deltaX;
  }

  public void refreshPosition() {
    if (currentSegment == null)
      return;

    // calculate travel
    updateDistanceTravelled();
    Position travel = Position.at(distanceTravelled, offsetFromCenter);

    // use current roadpiece's mapper to get relative position
    Position relative = currentSegment.mapOffsetToPosition(travel);

    // transform relative position to absolute
    position = currentSegment.getPosition().transform(relative);
  }

  public void setRoadmap(Roadmap roadmap) {
    this.roadmap = roadmap;
    if (roadmap == null) {
      onVehicleDelocalized(null);
    }
  }

  public Segment getCurrentSegment() {
    return currentSegment;
  }

  public int getSegmentPosInMap() {
	return segmentPosInMap;
  }

  public double getDistanceTravelled() {
    return distanceTravelled;
  }

  public LocalTime getLastRefresh() {
    return lastPositionRefresh;
  }

  @Override
  public String toString() {
    return anki.toString();
  }

}
