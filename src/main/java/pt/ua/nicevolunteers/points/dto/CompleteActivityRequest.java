package pt.ua.nicevolunteers.points.dto;

import java.util.UUID;

public record CompleteActivityRequest(
        UUID opportunityId,
        UUID volunteerId,
        String promoterEmail
) {
}
