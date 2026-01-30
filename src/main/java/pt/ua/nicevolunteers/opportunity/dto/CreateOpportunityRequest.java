package pt.ua.nicevolunteers.opportunity.dto;

public record CreateOpportunityRequest(
        String title,
        String promoterEmail,
        String description,
        int points,
        String location
) {
}
