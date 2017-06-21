package de.adesso.anki.console.learning;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;
import de.adesso.anki.console.tasks.SpeedTask;
import de.adesso.anki.console.tasks.VehicleTask;
import de.adesso.anki.messages.SegmentTimeMessage;

@Component
public class TrainingTask extends VehicleTask {

	private Map<String, TrainingListener> listeners = new HashMap<>();

	@Autowired
	private ApplicationContext context;

	@Autowired
	private SpeedTask speed;

	@Override
	public String getCommand() {
		return "trn";
	}

	@Override
	public String getDescription() {
		return "perform q-learning";
	}

	@Override
	public void run(Vehicle car, String... args) {
		String key = car.toString();
		if (!listeners.containsKey(key)) {
			TrainingListener listener = context.getBean(TrainingListener.class, car);
			car.addMessageListener(SegmentTimeMessage.class, listener);
			listeners.put(key, listener);
		}

		speed.setSpeed(car, AnkiAction.DEFAULT_ACTION.getSpeed());
	}

	public void fail(Vehicle car) {
		String key = car.toString();
		if (listeners.containsKey(key))
			listeners.get(key).fail();
	}

	public void cancel(Vehicle car) {
		String key = car.toString();
		if (listeners.containsKey(key))
			listeners.get(key).cancel();
	}
	
	public void setExploreRate(Vehicle car, double epsilon) {
		String key = car.toString();
		if (listeners.containsKey(key))
			listeners.get(key).setEpsilon(epsilon);		
	}
	
	public void setLearningRate(Vehicle car, double alpha) {
		String key = car.toString();
		if (listeners.containsKey(key))
			listeners.get(key).setAlpha(alpha);			
	}

	public void removeListener(Vehicle car) {
		car.removeMessageListener(SegmentTimeMessage.class, listeners.remove(car.toString()));
	}

}
