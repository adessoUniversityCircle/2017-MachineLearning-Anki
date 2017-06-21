package de.adesso.anki.console.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;
import de.adesso.anki.console.tasks.VehicleTask;

@Component
public class FailTask extends VehicleTask {

	@Autowired
	private TrainingTask training;

	@Override
	public String getCommand() {
		return "fail";
	}

	@Override
	public String getDescription() {
		return "notify failure";
	}

	@Override
	public void run(Vehicle car, String... args) {
		training.fail(car);
	}

}
