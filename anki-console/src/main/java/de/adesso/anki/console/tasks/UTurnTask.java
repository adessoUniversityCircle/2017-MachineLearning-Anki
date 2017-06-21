package de.adesso.anki.console.tasks;

import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;

/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class UTurnTask extends VehicleTask {

	@Override
	public String getCommand() {
		return "turn";
	}

	@Override
	public String getDescription() {
		return "perform a u-turn";
	}

	@Override
	public void run(Vehicle car, String... args) {
		if (car.isConnected())
			car.performUturn();
	}

}
