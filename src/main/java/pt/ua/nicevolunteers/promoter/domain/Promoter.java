package pt.ua.nicevolunteers.promoter.domain;

import jakarta.persistence.*;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidEmailException;

import java.util.UUID;

@Entity
@Table(name = "promoters")
public class Promoter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    @Column
    private String description;

    @Column
    private String website;

    @Column
    private String contactPhone;

    protected Promoter() {
        // JPA requires a no-arg constructor
    }

    public Promoter(String name, String email, String rawPassword) {
        if (!email.endsWith("@ua.pt")) {
            throw new InvalidEmailException();
        }

        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.hashedPassword = hashPassword(rawPassword);
    }

    private String hashPassword(String rawPassword) {
        // Simple hash for now - should use BCrypt in production
        return Integer.toHexString(rawPassword.hashCode());
    }

    public void updateProfile(String description, String website, String contactPhone) {
        this.description = description;
        this.website = website;
        this.contactPhone = contactPhone;
    }

    public void updatePassword(String newRawPassword) {
        this.hashedPassword = hashPassword(newRawPassword);
    }

    public boolean verifyPassword(String rawPassword) {
        return this.hashedPassword.equals(hashPassword(rawPassword));
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public String getContactPhone() {
        return contactPhone;
    }
}
