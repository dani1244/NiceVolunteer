package pt.ua.nicevolunteers.volunteer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidOpportunityException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;

@SpringBootTest
class OpportunityServiceTest {

    @Autowired
    private OpportunityService service;

    @Test
    void createOpportunity_success() {
        Opportunity op = service.createOpportunity(
                "Apoio a Conferência",
                "eventos@ua.pt",
                "Ajudar na organização",
                10,
                "DETI"
        );

        assertNotNull(op.getId());
        assertEquals("Apoio a Conferência", op.getTitle());
        assertEquals(10, op.getPoints());
    }

    @Test
    void createOpportunity_invalidTitle() {
        assertThrows(InvalidOpportunityException.class, () ->
                service.createOpportunity("", "eventos@ua.pt", "Desc", 10, "DETI"));
    }

    @Test
    void createOpportunity_invalidPoints() {
        assertThrows(InvalidOpportunityException.class, () ->
                service.createOpportunity("Evento", "eventos@ua.pt", "Desc", 0, "DETI"));
    }

    @Test
    void createOpportunity_invalidOrganization() {
        assertThrows(InvalidOpportunityException.class, () ->
                service.createOpportunity("Evento", "empresa@gmail.com", "Desc", 10, "Empresa"));
    }
}
