package pt.ua.nicevolunteers.volunteer.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RecommendationServiceTest {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Test
    void shouldRecommendOnlyOpenOpportunities() {

        opportunityRepository.deleteAll();
        volunteerRepository.deleteAll();

        Volunteer v = volunteerRepository.save(
                new Volunteer("Ana", "ana@ua.pt", "123456")
        );

        Opportunity openOp = opportunityRepository.save(
                new Opportunity("Evento A", "eventos@ua.pt", "DETI", 10, "UA", OpportunityStatus.OPEN)
        );

        Opportunity closedOp = opportunityRepository.save(
                new Opportunity("Evento B", "eventos@ua.pt", "DETI", 15, "UA", OpportunityStatus.CLOSED)
        );

        List<Opportunity> recommendations =
                recommendationService.recommendForVolunteer(v.getId());

        assertEquals(1, recommendations.size());
        assertEquals(openOp.getId(), recommendations.get(0).getId());
        assertNotEquals(closedOp.getId(), recommendations.get(0).getId());
    }
}
