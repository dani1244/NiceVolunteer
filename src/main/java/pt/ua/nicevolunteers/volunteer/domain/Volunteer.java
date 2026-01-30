package pt.ua.nicevolunteers.volunteer.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

    @Column(length = 500)
    private String bio;

    @ElementCollection
    private List<String> skills = new ArrayList<>();

    @ElementCollection
    private List<String> interests = new ArrayList<>();

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
