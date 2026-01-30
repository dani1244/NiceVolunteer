package pt.ua.nicevolunteers.application.domain;

public enum ApplicationStatus {
    PENDING,    // Application submitted, waiting for promoter decision
    ACCEPTED,   // Promoter accepted the application
    REJECTED,   // Promoter rejected the application
    COMPLETED   // Activity completed, points awarded
}
