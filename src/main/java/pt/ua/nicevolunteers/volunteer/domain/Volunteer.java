package pt.ua.nicevolunteers.volunteer.domain;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Volunteer {

    @Id
    private UUID id;

    private String name;
    private String email;
    private String hashedPassword;
    private int points = 0;

    protected Volunteer() { }

    public Volunteer(String name, String email, String hashedPassword) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public UUID getId() {


        return id;


    }
    public String getEmail() {

        return email;

    }
    public String getName() {

        return name;

    }
    public String getHashedPassword() {

        return hashedPassword;

    }
    public int getPoints() {

        return points;

    }

    public void addPoints(int points) {
        this.points += points;
    }
}
