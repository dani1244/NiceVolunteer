package pt.ua.nicevolunteers.volunteer.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.ua.nicevolunteers.application.domain.Application;
import pt.ua.nicevolunteers.application.domain.ApplicationStatus;
import pt.ua.nicevolunteers.application.repository.ApplicationRepository;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidApplicationException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@Service
@Transactional
public class ApplicationService {

    private final VolunteerRepository volunteerRepository;
    private final OpportunityRepository opportunityRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationService(VolunteerRepository volunteerRepository,
                              OpportunityRepository opportunityRepository,
                              ApplicationRepository applicationRepository) {
        this.volunteerRepository = volunteerRepository;
        this.opportunityRepository = opportunityRepository;
        this.applicationRepository = applicationRepository;
    }

    public Application apply(UUID volunteerId, UUID opportunityId) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(InvalidApplicationException::new);

        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(InvalidApplicationException::new);

        if (!opportunity.isOpen()) {
            throw new InvalidApplicationException();
        }

        if (applicationRepository.existsByVolunteerIdAndOpportunityId(volunteerId, opportunityId)) {
            throw new InvalidApplicationException();
        }

        Application application = new Application(volunteer, opportunity);
        return applicationRepository.save(application);
    }

    public List<Application> getVolunteerApplications(UUID volunteerId) {
        if (!volunteerRepository.existsById(volunteerId)) {
            throw new InvalidApplicationException();
        }
        return applicationRepository.findByVolunteerId(volunteerId);
    }

    public List<Application> getOpportunityApplications(UUID opportunityId) {
        if (!opportunityRepository.existsById(opportunityId)) {
            throw new InvalidApplicationException();
        }
        return applicationRepository.findByOpportunityId(opportunityId);
    }

    public Application acceptApplication(UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(InvalidApplicationException::new);

        application.accept();
        return applicationRepository.save(application);
    }

    public Application rejectApplication(UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(InvalidApplicationException::new);

        application.reject();
        return applicationRepository.save(application);
    }

    public Application completeApplication(UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(InvalidApplicationException::new);

        application.complete();
        return applicationRepository.save(application);
    }

    public long countApplications() {
        return applicationRepository.count();
    }

    public void deleteAllApplications() {
        applicationRepository.deleteAll();
    }

    public List<Application> getPendingApplicationsForOpportunity(UUID opportunityId) {
        return applicationRepository.findByOpportunityIdAndStatus(opportunityId, ApplicationStatus.PENDING);
    }
}
