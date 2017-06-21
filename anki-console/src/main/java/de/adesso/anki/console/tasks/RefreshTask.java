package de.adesso.anki.console.tasks;

import org.springframework.stereotype.Component;


/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class RefreshTask extends BasicTask {

	@Override
	public String getCommand() {
		return "ref";
	}

	@Override
	public String getDescription() {
		return "refresh the list of cars";
	}

	@Override
	public void run() {
		new Thread(() -> {
			log.info("Refreshing list of cars ...");
			cars.scanVehicles();
			log.info("Done");
		}).start();
	}

}
