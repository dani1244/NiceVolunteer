package pt.ua.nicevolunteers.volunteer.service;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidApplicationException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApplicationServiceTest {

    @Autowired
    private ApplicationService service;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Test
    void apply_success() {
        Volunteer v = volunteerRepository.save(new Volunteer("Ana", "ana@ua.pt", "123456"));
        Opportunity op = opportunityRepository.save(
                new Opportunity("Evento", "eventos@ua.pt", "Desc", 10, "DETI", OpportunityStatus.OPEN)
        );

        service.apply(v.getId(), op.getId());
        assertEquals(1, service.countApplications());
    }

    @Test
    void apply_twice_same_volunteer_same_opportunity_should_fail() {
        Volunteer v = volunteerRepository.save(new Volunteer("Ana", "ana@ua.pt", "123456"));
        Opportunity op = opportunityRepository.save(
                new Opportunity("Evento", "eventos@ua.pt", "Desc", 10, "DETI", OpportunityStatus.OPEN)
        );

        service.apply(v.getId(), op.getId());

        assertThrows(InvalidApplicationException.class, () ->
                service.apply(v.getId(), op.getId()));
    }

    @Test
    void apply_with_non_existing_volunteer_should_fail() {
        Opportunity op = opportunityRepository.save(
                new Opportunity("Evento", "eventos@ua.pt", "Desc", 10, "DETI", OpportunityStatus.OPEN)
        );

        assertThrows(InvalidApplicationException.class, () ->
                service.apply(UUID.randomUUID(), op.getId()));
    }

    @Test
    void apply_with_non_existing_opportunity_should_fail() {
        Volunteer v = volunteerRepository.save(new Volunteer("Ana", "ana@ua.pt", "123456"));

        assertThrows(InvalidApplicationException.class, () ->
                service.apply(v.getId(), UUID.randomUUID()));
    }
}
