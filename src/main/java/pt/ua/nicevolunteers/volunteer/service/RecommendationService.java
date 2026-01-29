package pt.ua.nicevolunteers.volunteer.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;

@Service
public class RecommendationService {

    private final OpportunityRepository opportunityRepository;

    public RecommendationService(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    public List<Opportunity> recommendForVolunteer(UUID volunteerId) {
        // US07: recomendar apenas oportunidades abertas
        return opportunityRepository.findByStatus(OpportunityStatus.OPEN);
    }
}
