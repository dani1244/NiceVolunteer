package pt.ua.nicevolunteers.volunteer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidOpportunityException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;

@Service
public class OpportunityService {

    private final OpportunityRepository repository;

    public OpportunityService(OpportunityRepository repository) {
        this.repository = repository;
    }

    public Opportunity createOpportunity(String title,
                                         String promoter,
                                         String description,
                                         int points,
                                         String location) {

        if (promoter == null || !promoter.endsWith("@ua.pt")) {
            throw new InvalidOpportunityException("Only institutional promoters allowed");
        }

        Opportunity op = new Opportunity(title, promoter, description, points, location);
        return repository.save(op);
    }

    public List<Opportunity> getOpenOpportunities() {
        return repository.findByStatus(OpportunityStatus.OPEN);
    }

    public void closeOpportunity(Opportunity opportunity) {
        if (!opportunity.isOpen()) {
            throw new InvalidOpportunityException("Opportunity is not open");
        }
        opportunity.close();
        repository.save(opportunity);
    }

    public void completeOpportunity(Opportunity opportunity) {
        opportunity.complete();
        repository.save(opportunity);
    }
}
