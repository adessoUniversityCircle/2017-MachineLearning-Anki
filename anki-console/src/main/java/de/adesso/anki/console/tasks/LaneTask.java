package de.adesso.anki.console.tasks;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;
import de.adesso.anki.util.LaneConverter;

/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class LaneTask extends VehicleTask {

	@Override
	public String getCommand() {
		return "ln";
	}

	@Override
	public String getDescription() {
		return "change the lane of a car";
	}

	@Override
	public List<String> getReqArgs() {
		return Arrays.asList("lane");
	}

	@Override
	public void run(Vehicle car, String... args) {
		try {
			double offset = LaneConverter.offsetFromLane(Integer.parseInt(args[0]));
			setOffset(car, offset);
		} catch (NumberFormatException nfe) {
			log.warn("not a number");
		}
	}

	public void setOffset(Vehicle car, double offset) {
		if (offset != 0 && offset != car.getDesiredOffset() && car.isConnected())
			car.changeLane(offset);
	}

}
