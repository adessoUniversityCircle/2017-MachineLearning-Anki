package de.adesso.anki.console.tasks;

import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;

/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class ConnectTask extends VehicleTask {

	@Override
	public String getCommand() {
		return "con";
	}

	@Override
	public String getDescription() {
		return "connect to a car";
	}

	@Override
	public void run(Vehicle car, String... args) {
		if (!car.isConnected())
			car.connect();
	}

}
