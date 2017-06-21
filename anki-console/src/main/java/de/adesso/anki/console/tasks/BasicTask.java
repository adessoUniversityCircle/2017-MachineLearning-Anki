package de.adesso.anki.console.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.anki.VehiclePool;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class BasicTask implements ConsoleTask {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected VehiclePool cars;

	@Override
	public void run(String... args) {
		run();
	}

	public abstract void run();

}
