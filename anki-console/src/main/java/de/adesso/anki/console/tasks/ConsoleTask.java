package de.adesso.anki.console.tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Manuel Barbi
 *
 */
public interface ConsoleTask {

	String getCommand();

	String getDescription();

	default List<String> getReqArgs() {
		return new ArrayList<>();
	}

	void run(String... args);

}
