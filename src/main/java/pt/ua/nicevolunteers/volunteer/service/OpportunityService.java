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

    public Opportunity create(String title,
                              String description,
                              String promoter,
                              int points,
                              String email) {

        if (title == null || title.isBlank()) {
            throw new InvalidOpportunityException("Title is mandatory");
        }

        if (points <= 0) {
            throw new InvalidOpportunityException("Points must be positive");
        }

        if (!email.endsWith("@ua.pt")) {
            throw new InvalidOpportunityException("Promoter must be an institutional UA organization");
        }

        Opportunity opportunity = new Opportunity(title, description, promoter, points, email);
        return repository.save(opportunity);
    }
}
