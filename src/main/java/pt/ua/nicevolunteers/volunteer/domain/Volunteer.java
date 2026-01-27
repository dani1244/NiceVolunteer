package pt.ua.nicevolunteers.volunteer.domain;

import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import pt.ua.nicevolunteers.volunteer.domain.valueobject.Email;
import pt.ua.nicevolunteers.volunteer.domain.valueobject.Password;

@Entity
public class Volunteer {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    protected Volunteer() {
        // JPA
    }

    public Volunteer(String name, String email, String password) {
        this.name = name;
        this.email = new Email(email);
        this.password = new Password(password);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getHashedPassword() {
        return password.getHashedValue();
    }
}
