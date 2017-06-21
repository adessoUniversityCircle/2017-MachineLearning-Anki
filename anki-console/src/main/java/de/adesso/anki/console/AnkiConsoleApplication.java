package de.adesso.anki.console;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import de.adesso.anki.VehicleGateway;
import de.adesso.anki.VehiclePool;
import de.adesso.anki.console.tasks.ConsoleTask;
import de.adesso.anki.console.tasks.HelpTask;

@SpringBootApplication
public class AnkiConsoleApplication {

	protected static final Logger log = LoggerFactory.getLogger(AnkiConsoleApplication.class);
	protected static ApplicationContext context;

	public static final String WLAN_IP = "192.168.1.22";
	public static final String LAN_IP = "169.254.30.66";
	public static final int PORT = 5000;
	
	@Value("#{'${anki-console.gateways}'.split(',')}") 
	private List<String> gatewayIps;

	@Autowired
	protected List<ConsoleTask> tasks;
	protected Scanner sc;

	public static void main(String[] args) throws IOException {
		context = SpringApplication.run(AnkiConsoleApplication.class, args);
	}

	@Bean
	public VehiclePool createPool() throws IOException {
		log.info("Connecting to gateway ...");
		return new VehiclePool(new VehicleGateway(gatewayIps.stream().toArray(String[]::new)));
	}

	@Bean
	public CommandLineRunner run() {
		return (args) -> {
			new Thread(() -> {
				Map<String, ConsoleTask> cmd = new LinkedHashMap<>();

				for (ConsoleTask t : tasks)
					cmd.put(t.getCommand(), t);

				this.sc = new Scanner(System.in);
				cmd.get(HelpTask.CMD).run();

				String[] tkn;
				log.info("Enter command ...");

				while (!(tkn = sc.nextLine().toLowerCase().split(" "))[0].equals("exit")) {

					try {
						if (!cmd.containsKey(tkn[0])) {
							log.warn("Unknown command");
							continue;
						}

						cmd.get(tkn[0]).run(tkn);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}

					log.info("Enter command ...");
				}

				SpringApplication.exit(context);
			}).start();
		};
	}

}
