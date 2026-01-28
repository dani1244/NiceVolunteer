package pt.ua.nicevolunteers.volunteer.domain.opportunity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidOpportunityException;

@Entity
public class Opportunity {

    @Id
    private UUID id;

    private String title;
    private String promoter;
    private String description;
    private int points;
    private String location;

    protected Opportunity() {}

    public Opportunity(String title, String promoter, String description, int points, String location) {

        if (title == null || title.isBlank()) {
            throw new InvalidOpportunityException("Title is required");
        }

        if (promoter == null || promoter.isBlank()) {
            throw new InvalidOpportunityException("Promoter is required");
        }

        if (points <= 0) {
            throw new InvalidOpportunityException("Points must be positive");
        }

        this.id = UUID.randomUUID();
        this.title = title;
        this.promoter = promoter;
        this.description = description;
        this.points = points;
        this.location = location;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getPromoter() { return promoter; }
    public String getDescription() { return description; }
    public int getPoints() { return points; }
    public String getLocation() { return location; }
}
