package de.adesso.anki.console.tasks;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Manuel Barbi
 *
 */
@Component
public class HelpTask extends BasicTask {

	public static final String CMD = "help";

	@Autowired
	private List<ConsoleTask> tasks;

	@PostConstruct
	protected void sort() {
		Collections.sort(tasks, (t1, t2) -> {
			int result;

			if ((result = -Boolean.compare(t1 instanceof BasicTask, t2 instanceof BasicTask)) != 0)
				return result;

			return t1.getClass().getSimpleName().compareTo(t2.getClass().getSimpleName());
		});
	}

	@Override
	public String getCommand() {
		return CMD;
	}

	@Override
	public String getDescription() {
		return "show list of commands";
	}

	@Override
	public void run() {
		StringBuilder sb = new StringBuilder("List of commands:\n");

		sb.append("\t'");
		sb.append(CMD);
		sb.append("' to ");
		sb.append(getDescription());
		sb.append("\n");

		for (ConsoleTask t : tasks) {
			sb.append("\t'");
			sb.append(t.getCommand());

			if (t instanceof VehicleTask)
				sb.append(" [car-id]");

			for (String req : t.getReqArgs()) {
				sb.append(" [");
				sb.append(req);
				sb.append("]");
			}

			sb.append("' to ");
			sb.append(t.getDescription());
			sb.append("\n");
		}
		sb.append("\t'exit' to close the application");

		log.info(sb.toString());
	}

}
