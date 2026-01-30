package pt.ua.nicevolunteers.application.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunity opportunity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    @Column
    private LocalDateTime respondedAt;

    protected Application() {
        // JPA requires a no-arg constructor
    }

    public Application(Volunteer volunteer, Opportunity opportunity) {
        this.id = UUID.randomUUID();
        this.volunteer = volunteer;
        this.opportunity = opportunity;
        this.status = ApplicationStatus.PENDING;
        this.appliedAt = LocalDateTime.now();
    }

    public void accept() {
        if (this.status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Can only accept pending applications");
        }
        this.status = ApplicationStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void reject() {
        if (this.status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Can only reject pending applications");
        }
        this.status = ApplicationStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.status != ApplicationStatus.ACCEPTED) {
            throw new IllegalStateException("Can only complete accepted applications");
        }
        this.status = ApplicationStatus.COMPLETED;
    }

    public UUID getId() {
        return id;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public boolean isPending() {
        return status == ApplicationStatus.PENDING;
    }

    public boolean isAccepted() {
        return status == ApplicationStatus.ACCEPTED;
    }

    public boolean isRejected() {
        return status == ApplicationStatus.REJECTED;
    }

    public boolean isCompleted() {
        return status == ApplicationStatus.COMPLETED;
    }
}
