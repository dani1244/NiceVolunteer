package pt.ua.nicevolunteers.volunteer.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidApplicationException;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidOpportunityException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@Service
public class ActivityCompletionService {

    private final VolunteerRepository volunteerRepository;
    private final OpportunityRepository opportunityRepository;

    private final Set<String> applications = new HashSet<>();
    private final Set<String> accepted = new HashSet<>();
    private final Set<String> completed = new HashSet<>();

    public ActivityCompletionService(VolunteerRepository volunteerRepository,
                                     OpportunityRepository opportunityRepository) {
        this.volunteerRepository = volunteerRepository;
        this.opportunityRepository = opportunityRepository;
    }

    public void apply(UUID volunteerId, UUID opportunityId) {
        applications.add(volunteerId + ":" + opportunityId);
    }

    public void acceptApplication(UUID volunteerId, UUID opportunityId) {
        String key = volunteerId + ":" + opportunityId;
        if (!applications.contains(key)) {
            throw new InvalidApplicationException();
        }
        accepted.add(key);
    }

    public void completeActivity(UUID opportunityId, UUID volunteerId, String promoterEmail) {

        String key = volunteerId + ":" + opportunityId;

        if (!accepted.contains(key)) {
            throw new InvalidApplicationException();
        }

        if (completed.contains(key)) {
            throw new InvalidOpportunityException();
        }

        Opportunity op = opportunityRepository.findById(opportunityId)
                .orElseThrow(InvalidOpportunityException::new);

        if (!op.getLocation().equals(promoterEmail)) {
            throw new InvalidOpportunityException();
        }

        Volunteer v = volunteerRepository.findById(volunteerId)
                .orElseThrow(InvalidApplicationException::new);

        v.addPoints(op.getPoints());
        volunteerRepository.save(v);

        completed.add(key);
    }
}
