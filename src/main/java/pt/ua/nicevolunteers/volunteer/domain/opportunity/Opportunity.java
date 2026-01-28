package pt.ua.nicevolunteers.volunteer.domain.opportunity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidOpportunityException;

@Entity
public class Opportunity {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;
    private String location;
    private int points;
    private String promoter;

    protected Opportunity() {}

    public Opportunity(String title, String description, String location, int points, String promoter) {

        if (title == null || title.isBlank())
            throw new InvalidOpportunityException("Title is mandatory");

        if (description == null || description.isBlank())
            throw new InvalidOpportunityException("Description is mandatory");

        if (location == null || location.isBlank())
            throw new InvalidOpportunityException("Location is mandatory");

        if (promoter == null || promoter.isBlank())
            throw new InvalidOpportunityException("Promoter is mandatory");

        // REGRA DE DOMÍNIO: promotor tem de ser institucional UA
        if (!promoter.toLowerCase().contains("ua.pt"))
            throw new InvalidOpportunityException("Promoter must be an institutional UA organization");

        if (points <= 0)
            throw new InvalidOpportunityException("Points must be positive");

        this.title = title;
        this.description = description;
        this.location = location;
        this.points = points;
        this.promoter = promoter;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public int getPoints() { return points; }
    public String getPromoter() { return promoter; }
}
