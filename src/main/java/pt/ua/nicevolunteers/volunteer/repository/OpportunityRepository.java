package pt.ua.nicevolunteers.volunteer.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;

public interface OpportunityRepository extends JpaRepository<Opportunity, UUID> {

    List<Opportunity> findByStatus(OpportunityStatus status);
}
