package pt.ua.nicevolunteers.points.dto;

import java.util.UUID;

public record PointsBalanceResponse(
        UUID volunteerId,
        int currentBalance
) {
}
