package de.adesso.anki.console.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;
import de.adesso.anki.console.tasks.VehicleTask;

@Component
public class CancelTask extends VehicleTask {

	@Autowired
	private TrainingTask training;

	@Override
	public String getCommand() {
		return "cnl";
	}

	@Override
	public String getDescription() {
		return "notify cancel";
	}

	@Override
	public void run(Vehicle car, String... args) {
		training.cancel(car);
	}

}
