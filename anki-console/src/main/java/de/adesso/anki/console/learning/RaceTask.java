package de.adesso.anki.console.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;
import de.adesso.anki.console.tasks.VehicleTask;

@Component
public class RaceTask extends VehicleTask {

	@Autowired
	private TrainingTask training;

	@Override
	public String getCommand() {
		return "race";
	}

	@Override
	public String getDescription() {
		return "start race mode";
	}

	@Override
	public void run(Vehicle car, String... args) {
		training.setExploreRate(car, 0);
		training.setLearningRate(car, 0);
	}

}
