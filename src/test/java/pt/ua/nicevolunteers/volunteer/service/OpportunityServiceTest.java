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
        Opportunity op = service.create(
                "Apoio a Conferência",
                "Ajudar na organização",
                "DETI",
                10,
                "eventos@ua.pt"
        );

        assertNotNull(op.getId());
        assertEquals("Apoio a Conferência", op.getTitle());
        assertEquals(10, op.getPoints());
    }

    @Test
    void createOpportunity_invalidTitle() {
        assertThrows(InvalidOpportunityException.class, () ->
                service.create("", "Desc", "DETI", 10, "deti@ua.pt"));
    }

    @Test
    void createOpportunity_invalidPoints() {
        assertThrows(InvalidOpportunityException.class, () ->
                service.create("Evento", "Desc", "DETI", 0, "deti@ua.pt"));
    }

    @Test
    void createOpportunity_invalidOrganization() {
        assertThrows(InvalidOpportunityException.class, () ->
                service.create("Evento", "Desc", "Empresa", 10, "empresa@gmail.com"));
    }
}
