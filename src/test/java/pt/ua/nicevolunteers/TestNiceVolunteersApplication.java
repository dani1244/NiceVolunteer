package pt.ua.nicevolunteers;

import org.springframework.boot.SpringApplication;

public class TestNiceVolunteersApplication {

	public static void main(String[] args) {
		SpringApplication.from(NiceVolunteersApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
