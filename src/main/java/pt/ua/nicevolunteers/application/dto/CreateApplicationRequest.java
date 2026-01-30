package pt.ua.nicevolunteers.application.dto;

import java.util.UUID;

public record CreateApplicationRequest(
        UUID volunteerId,
        UUID opportunityId
) {
}
