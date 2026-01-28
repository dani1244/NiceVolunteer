package pt.ua.nicevolunteers.volunteer.domain.points;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;

@Entity
public class PointsTransaction {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Volunteer volunteer;

    private int points;
    private String description;
    private LocalDateTime timestamp;

    protected PointsTransaction() {}

    public PointsTransaction(Volunteer volunteer, int points, String description) {
        this.volunteer = volunteer;
        this.points = points;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public int getPoints() {
        return points;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
