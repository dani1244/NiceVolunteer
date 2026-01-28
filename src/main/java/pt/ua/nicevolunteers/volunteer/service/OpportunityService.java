package pt.ua.nicevolunteers.volunteer.service;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidOpportunityException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;

@Service
public class OpportunityService {

    private final OpportunityRepository repository;

    public OpportunityService(OpportunityRepository repository) {
        this.repository = repository;
    }

    public Opportunity create(String title, String description, String organization, int points, String contactEmail) {

        if (title == null || title.isBlank()) {
            throw new InvalidOpportunityException("Title cannot be empty");
        }

        if (points <= 0) {
            throw new InvalidOpportunityException("Points must be positive");
        }

        boolean validOrganization = organization != null && organization.toUpperCase().contains("UA");
        boolean validEmail = contactEmail != null && contactEmail.endsWith("@ua.pt");

        if (!validOrganization && !validEmail) {
            throw new InvalidOpportunityException("Promoter must be an institutional UA organization");
        }

        Opportunity opportunity = new Opportunity(title, description, organization, points, contactEmail);
        return repository.save(opportunity);
    }
}
