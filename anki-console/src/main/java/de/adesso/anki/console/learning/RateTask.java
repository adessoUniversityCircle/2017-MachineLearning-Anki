package de.adesso.anki.console.learning;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;
import de.adesso.anki.console.tasks.VehicleTask;

@Component
public class RateTask extends VehicleTask {

	@Autowired
	private TrainingTask training;

	@Override
	public String getCommand() {
		return "rate";
	}

	@Override
	public String getDescription() {
		return "set explore rate";
	}

	@Override
	public List<String> getReqArgs() {
		return Arrays.asList("explore-rate");
	}

	@Override
	public void run(Vehicle car, String... args) {
		try {
			double epsilon = Double.parseDouble(args[0]);
			training.setExploreRate(car, epsilon);
		} catch (NumberFormatException nfe) {
			log.warn("not a number");
		}
	}

}
