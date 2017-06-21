package de.adesso.anki.console.tasks;

import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;

/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class StopTask extends VehicleTask {

	@Override
	public String getCommand() {
		return "stop";
	}

	@Override
	public String getDescription() {
		return "stop a car";
	}

	@Override
	public void run(Vehicle car, String... args) {
		if (car.isConnected())
			car.stop();
	}

}
