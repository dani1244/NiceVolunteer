package pt.ua.nicevolunteers.volunteer.service;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidApplicationException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@SpringBootTest
class ApplicationServiceTest {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @BeforeEach
    void cleanDatabase() {
        applicationService.deleteAllApplications();
        opportunityRepository.deleteAll();
        volunteerRepository.deleteAll();
    }

    @Test
    void apply_success() {
        Volunteer v = volunteerRepository.save(
                new Volunteer("Ana", "ana@ua.pt", "password123"));

        Opportunity o = opportunityRepository.save(
                new Opportunity("Mentoria",
                        "deti@ua.pt",          // promoter CORRETO
                        "Apoio a alunos",
                        10,
                        "Aveiro"));

        applicationService.apply(v.getId(), o.getId());

        assertEquals(1, applicationService.countApplications());
    }

    @Test
    void apply_twice_same_volunteer_same_opportunity_should_fail() {
        Volunteer v = volunteerRepository.save(
                new Volunteer("Ana", "ana@ua.pt", "password123"));

        Opportunity o = opportunityRepository.save(
                new Opportunity("Mentoria",
                        "deti@ua.pt",
                        "Apoio a alunos",
                        10,
                        "Aveiro"));

        applicationService.apply(v.getId(), o.getId());

        assertThrows(InvalidApplicationException.class,
                () -> applicationService.apply(v.getId(), o.getId()));
    }

    @Test
    void apply_with_non_existing_volunteer_should_fail() {
        Opportunity o = opportunityRepository.save(
                new Opportunity("Mentoria",
                        "deti@ua.pt",
                        "Apoio a alunos",
                        10,
                        "Aveiro"));

        assertThrows(InvalidApplicationException.class,
                () -> applicationService.apply(UUID.randomUUID(), o.getId()));
    }

    @Test
    void apply_with_non_existing_opportunity_should_fail() {
        Volunteer v = volunteerRepository.save(
                new Volunteer("Ana", "ana@ua.pt", "password123"));

        assertThrows(InvalidApplicationException.class,
                () -> applicationService.apply(v.getId(), UUID.randomUUID()));
    }
}
