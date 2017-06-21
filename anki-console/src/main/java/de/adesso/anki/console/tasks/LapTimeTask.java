package de.adesso.anki.console.tasks;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;
import de.adesso.anki.messages.LapTimeMessage;
import de.adesso.anki.sdk.messages.MessageListener;

/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class LapTimeTask extends VehicleTask {

	protected Map<String, MessageListener<LapTimeMessage>> listeners = new HashMap<>();

	@Override
	public String getCommand() {
		return "lap";
	}

	@Override
	public String getDescription() {
		return "measure lap time";
	}

	@Override
	public void run(Vehicle car, String... args) {
		String key = car.toString();
		if (!listeners.containsKey(key)) {
			MessageListener<LapTimeMessage> lst = (m) -> {
				System.out.println("lap time for " + key + ": " + m.getLapTime() + " ms");
			};
			car.addMessageListener(LapTimeMessage.class, lst);
			listeners.put(key, lst);
		}
	}

	public void remove(Vehicle car) {
		car.removeMessageListener(LapTimeMessage.class, listeners.remove(car.toString()));
	}

}
