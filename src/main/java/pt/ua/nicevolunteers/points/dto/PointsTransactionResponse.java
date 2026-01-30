package pt.ua.nicevolunteers.points.dto;

import pt.ua.nicevolunteers.volunteer.domain.points.PointsTransaction;

import java.time.LocalDateTime;
import java.util.UUID;

public record PointsTransactionResponse(
        UUID id,
        UUID volunteerId,
        int points,
        String description,
        LocalDateTime timestamp
) {
    public static PointsTransactionResponse fromEntity(PointsTransaction transaction) {
        return new PointsTransactionResponse(
                transaction.getId(),
                transaction.getVolunteer().getId(),
                transaction.getPoints(),
                transaction.getDescription(),
                transaction.getTimestamp()
        );
    }
}
