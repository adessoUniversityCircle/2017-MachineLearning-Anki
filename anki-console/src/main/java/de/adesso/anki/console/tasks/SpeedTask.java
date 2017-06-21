package de.adesso.anki.console.tasks;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;

/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class SpeedTask extends VehicleTask {

	@Autowired
	protected ConnectTask connect;

	@Override
	public String getCommand() {
		return "spd";
	}

	@Override
	public String getDescription() {
		return "change the speed of a car";
	}

	@Override
	public List<String> getReqArgs() {
		return Arrays.asList("speed");
	}

	@Override
	public void run(Vehicle car, String... args) {
		try {
			int speed = Integer.parseInt(args[0]);
			setSpeed(car, speed);
		} catch (NumberFormatException nfe) {
			log.warn("not a number");
		}
	}

	public void setSpeed(Vehicle car, int speed) {
		connect.run(car);
		if (speed >= 0 && speed <= 5000) {
			car.setDesiredSpeed(speed);
		}
	}

}
