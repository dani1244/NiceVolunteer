package pt.ua.nicevolunteers.volunteer.domain.opportunity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String category;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private OpportunityStatus status;

    protected Opportunity() { }

    public Opportunity(String title, String promoter, String description, int points, String location) {
        this(title, promoter, description, points, location, null, null, OpportunityStatus.OPEN);
    }

    public Opportunity(String title, String promoter, String description, int points,
                       String location, OpportunityStatus status) {
        this(title, promoter, description, points, location, null, null, status);
    }

    public Opportunity(String title, String promoter, String description, int points,
                       String location, String category, LocalDate date, OpportunityStatus status) {

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
        this.category = category;
        this.date = date;
        this.status = status;
    }

    public UUID getId() {


        return id;


    }
    public String getTitle() {

        return title;

    }
    public String getPromoter() {

        return promoter;

    }
    public String getDescription() {

        return description;

    }
    public int getPoints() {

        return points;

    }
    public String getLocation() {

        return location;

    }
    public String getCategory() {

        return category;

    }
    public LocalDate getDate() {

        return date;

    }
    public OpportunityStatus getStatus() {

        return status;

    }

    public void close() {
        this.status = OpportunityStatus.CLOSED;
    }

    public void complete() {
        if (this.status != OpportunityStatus.OPEN) {
            throw new InvalidOpportunityException("Only open opportunities can be completed");
        }
        this.status = OpportunityStatus.COMPLETED;
    }

    public boolean isOpen() {


        return this.status == OpportunityStatus.OPEN;


    }
}
