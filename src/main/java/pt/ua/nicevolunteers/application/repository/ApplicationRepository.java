package pt.ua.nicevolunteers.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ua.nicevolunteers.application.domain.Application;
import pt.ua.nicevolunteers.application.domain.ApplicationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findByVolunteerId(UUID volunteerId);

    List<Application> findByOpportunityId(UUID opportunityId);

    Optional<Application> findByVolunteerIdAndOpportunityId(UUID volunteerId, UUID opportunityId);

    List<Application> findByStatus(ApplicationStatus status);

    List<Application> findByVolunteerIdAndStatus(UUID volunteerId, ApplicationStatus status);

    List<Application> findByOpportunityIdAndStatus(UUID opportunityId, ApplicationStatus status);

    long countByOpportunityId(UUID opportunityId);

    long countByVolunteerId(UUID volunteerId);

    boolean existsByVolunteerIdAndOpportunityId(UUID volunteerId, UUID opportunityId);
}
