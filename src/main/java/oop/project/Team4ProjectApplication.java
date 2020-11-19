package oop.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Team4ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Team4ProjectApplication.class, args);
		PortnetConnector.dailyTask();
		PortnetConnector.hourlyTask();
	}

}
