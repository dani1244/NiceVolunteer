package pt.ua.nicevolunteers.volunteer.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidApplicationException;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidOpportunityException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@SpringBootTest
class ActivityCompletionServiceTest {

    @Autowired
    private ActivityCompletionService service;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Test
    void completeActivity_success() {
        Volunteer v = volunteerRepository.save(
                new Volunteer("Ana", "ana@ua.pt", "123456")
        );

        Opportunity op = opportunityRepository.save(
                new Opportunity("Apoio Evento", "Ajuda logística", "DETI", 20, "eventos@ua.pt")
        );

        service.apply(v.getId(), op.getId());
        service.acceptApplication(v.getId(), op.getId());

        int previousPoints = v.getPoints();

        service.completeActivity(op.getId(), v.getId(), "eventos@ua.pt");

        Volunteer updated = volunteerRepository.findById(v.getId()).orElseThrow();
        assertEquals(previousPoints + 20, updated.getPoints());
    }

    @Test
    void completeActivity_nonAcceptedVolunteer_shouldFail() {
        Volunteer v = volunteerRepository.save(
                new Volunteer("Rui", "rui@ua.pt", "123456")
        );

        Opportunity op = opportunityRepository.save(
                new Opportunity("Workshop", "Suporte", "DETI", 10, "workshop@ua.pt")
        );

        assertThrows(InvalidApplicationException.class, () ->
                service.completeActivity(op.getId(), v.getId(), "workshop@ua.pt"));
    }

    @Test
    void completeActivity_wrongPromoter_shouldFail() {
        Volunteer v = volunteerRepository.save(
                new Volunteer("Ines", "ines@ua.pt", "123456")
        );

        Opportunity op = opportunityRepository.save(
                new Opportunity("Seminário", "Apoio", "DETI", 15, "seminarios@ua.pt")
        );

        service.apply(v.getId(), op.getId());
        service.acceptApplication(v.getId(), op.getId());

        assertThrows(InvalidOpportunityException.class, () ->
                service.completeActivity(op.getId(), v.getId(), "fake@ua.pt"));
    }

    @Test
    void completeActivity_twice_shouldFail() {
        Volunteer v = volunteerRepository.save(
                new Volunteer("Pedro", "pedro@ua.pt", "123456")
        );

        Opportunity op = opportunityRepository.save(
                new Opportunity("Feira", "Organização", "DETI", 30, "feira@ua.pt")
        );

        service.apply(v.getId(), op.getId());
        service.acceptApplication(v.getId(), op.getId());
        service.completeActivity(op.getId(), v.getId(), "feira@ua.pt");

        assertThrows(InvalidOpportunityException.class, () ->
                service.completeActivity(op.getId(), v.getId(), "feira@ua.pt"));
    }
}
