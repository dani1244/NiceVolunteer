package pt.ua.nicevolunteers.volunteer.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidApplicationException;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@Service
public class ApplicationService {

    private final VolunteerRepository volunteerRepository;
    private final OpportunityRepository opportunityRepository;

    private final Set<String> applications = new HashSet<>();

    public ApplicationService(VolunteerRepository volunteerRepository,
                              OpportunityRepository opportunityRepository) {
        this.volunteerRepository = volunteerRepository;
        this.opportunityRepository = opportunityRepository;
    }

    public void apply(UUID volunteerId, UUID opportunityId) {

        if (!volunteerRepository.existsById(volunteerId)) {
            throw new InvalidApplicationException("Volunteer does not exist");
        }

        if (!opportunityRepository.existsById(opportunityId)) {
            throw new InvalidApplicationException("Opportunity does not exist");
        }

        String key = volunteerId + ":" + opportunityId;

        if (applications.contains(key)) {
            throw new InvalidApplicationException("Volunteer already applied to this opportunity");
        }

        applications.add(key);
    }

    public int countApplications() {
        return applications.size();
    }

    public void deleteAllApplications() {
        applications.clear();
    }
}
