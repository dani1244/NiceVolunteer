package pt.ua.nicevolunteers.opportunity.dto;

import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;

import java.util.UUID;

public record OpportunityResponse(
        UUID id,
        String title,
        String promoter,
        String description,
        int points,
        String location,
        OpportunityStatus status
) {
    public static OpportunityResponse fromEntity(Opportunity opportunity) {
        return new OpportunityResponse(
                opportunity.getId(),
                opportunity.getTitle(),
                opportunity.getPromoter(),
                opportunity.getDescription(),
                opportunity.getPoints(),
                opportunity.getLocation(),
                opportunity.getStatus()
        );
    }
}
