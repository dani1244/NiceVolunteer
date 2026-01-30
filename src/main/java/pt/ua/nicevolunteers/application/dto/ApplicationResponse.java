package pt.ua.nicevolunteers.application.dto;

import pt.ua.nicevolunteers.application.domain.Application;
import pt.ua.nicevolunteers.application.domain.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApplicationResponse(
        UUID id,
        UUID volunteerId,
        String volunteerName,
        UUID opportunityId,
        String opportunityTitle,
        ApplicationStatus status,
        LocalDateTime appliedAt,
        LocalDateTime respondedAt
) {
    public static ApplicationResponse fromEntity(Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getVolunteer().getId(),
                application.getVolunteer().getName(),
                application.getOpportunity().getId(),
                application.getOpportunity().getTitle(),
                application.getStatus(),
                application.getAppliedAt(),
                application.getRespondedAt()
        );
    }
}
