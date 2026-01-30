package pt.ua.nicevolunteers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NiceVolunteersApplication {

    private NiceVolunteersApplication() {
        // Private constructor to hide implicit public one
    }

    public static void main(String[] args) {
        SpringApplication.run(NiceVolunteersApplication.class, args);
    }

}
