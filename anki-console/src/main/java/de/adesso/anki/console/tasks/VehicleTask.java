package de.adesso.anki.console.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.anki.Vehicle;
import de.adesso.anki.VehiclePool;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class VehicleTask implements ConsoleTask {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected VehiclePool cars;

	private static String lastCar;

	public void run(String... args) {
		List<String> req = getReqArgs();
		if (args.length < req.size() + 1) {
			log.warn("not enough parameters");
			return;
		}
		
		String requestedCar;
		boolean useLast = false;
		if (args.length == req.size() + 1) {
			if (lastCar == null) {
				log.warn("no car specified");
				return;
			}
			useLast = true;
				
			requestedCar = lastCar;
		} else {
			requestedCar = args[1];
		}
		

		if (!cars.checkId(requestedCar)) {
			log.warn("unknown id");
			return;
		}

		String[] trim = new String[args.length - (useLast ? 1 : 2)];
		System.arraycopy(args, (useLast ? 1 : 2), trim, 0, trim.length);
		run(cars.get(requestedCar), trim);
		
		lastCar = requestedCar;
	}

	public abstract void run(Vehicle car, String... args);

}
