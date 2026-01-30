package pt.ua.nicevolunteers.recommendation.dto;

import pt.ua.nicevolunteers.opportunity.dto.OpportunityResponse;

import java.util.List;
import java.util.UUID;

public record RecommendationResponse(
        UUID volunteerId,
        List<OpportunityResponse> recommendedOpportunities
) {
}
