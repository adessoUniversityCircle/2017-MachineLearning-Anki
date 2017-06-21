package de.adesso.anki.console.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;

/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class DisconnectTask extends VehicleTask {

	@Autowired
	protected StopTask stop;

	@Override
	public String getCommand() {
		return "dis";
	}

	@Override
	public String getDescription() {
		return "disconnect from a car";
	}

	@Override
	public void run(Vehicle car, String... args) {
		cars.saveDisconnect(car);
	}

}
