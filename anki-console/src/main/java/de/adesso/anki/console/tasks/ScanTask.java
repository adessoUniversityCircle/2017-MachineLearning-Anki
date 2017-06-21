package de.adesso.anki.console.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.adesso.anki.Vehicle;
import de.adesso.anki.roadmap.RoadmapScanner;

/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class ScanTask extends VehicleTask {

	@Autowired
	protected ConnectTask connect;

	@Override
	public String getCommand() {
		return "scan";
	}

	@Override
	public String getDescription() {
		return "scan the track";
	}

	@Override
	public void run(Vehicle car, String... args) {
		log.info("Scanning track ...");
		new Thread(() -> {
			connect.run(car);

			RoadmapScanner scanner = new RoadmapScanner(car);
			scanner.startScanning();

			try {
				int ctx = 0;
				while (!scanner.isComplete() && ctx < 30) {
					ctx++;
					Thread.sleep(500);
				}

				car.setRoadmap(scanner.getRoadmap());
				log.info("Done");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}).start();
	}

}
