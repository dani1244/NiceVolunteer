package pt.ua.nicevolunteers.application.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ua.nicevolunteers.application.domain.Application;
import pt.ua.nicevolunteers.application.domain.ApplicationStatus;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ApplicationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApplicationRepository applicationRepository;

    private Volunteer volunteer;
    private Opportunity opportunity;

    @BeforeEach
    void setUp() {
        volunteer = new Volunteer("John Doe", "john@ua.pt", "password123");
        volunteer = entityManager.persist(volunteer);

        opportunity = new Opportunity("Beach Cleanup", "promoter@ua.pt", "Clean beach", 10, "Aveiro");
        opportunity = entityManager.persist(opportunity);

        entityManager.flush();
    }

    @Test
    void findByVolunteerId_ShouldReturnApplications() {
        // Arrange
        Application app1 = new Application(volunteer, opportunity);
        applicationRepository.save(app1);

        Opportunity opp2 = new Opportunity("Food Bank", "promoter@ua.pt", "Help", 15, "Porto");
        opp2 = entityManager.persist(opp2);
        Application app2 = new Application(volunteer, opp2);
        applicationRepository.save(app2);

        // Act
        List<Application> result = applicationRepository.findByVolunteerId(volunteer.getId());

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Application::getVolunteer).containsOnly(volunteer);
    }

    @Test
    void findByOpportunityId_ShouldReturnApplications() {
        // Arrange
        Application app1 = new Application(volunteer, opportunity);
        applicationRepository.save(app1);

        Volunteer vol2 = new Volunteer("Jane Doe", "jane@ua.pt", "password456");
        vol2 = entityManager.persist(vol2);
        Application app2 = new Application(vol2, opportunity);
        applicationRepository.save(app2);

        // Act
        List<Application> result = applicationRepository.findByOpportunityId(opportunity.getId());

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Application::getOpportunity).containsOnly(opportunity);
    }

    @Test
    void findByVolunteerIdAndOpportunityId_ShouldReturnApplication() {
        // Arrange
        Application application = new Application(volunteer, opportunity);
        applicationRepository.save(application);

        // Act
        Optional<Application> result = applicationRepository.findByVolunteerIdAndOpportunityId(
                volunteer.getId(), opportunity.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getVolunteer()).isEqualTo(volunteer);
        assertThat(result.get().getOpportunity()).isEqualTo(opportunity);
    }

    @Test
    void findByStatus_ShouldReturnApplicationsWithStatus() {
        // Arrange
        Application pendingApp = new Application(volunteer, opportunity);
        applicationRepository.save(pendingApp);

        Opportunity opp2 = new Opportunity("Food Bank", "promoter@ua.pt", "Help", 15, "Porto");
        opp2 = entityManager.persist(opp2);
        Volunteer vol2 = new Volunteer("Jane Doe", "jane@ua.pt", "password456");
        vol2 = entityManager.persist(vol2);
        Application acceptedApp = new Application(vol2, opp2);
        acceptedApp.accept();
        applicationRepository.save(acceptedApp);

        // Act
        List<Application> pending = applicationRepository.findByStatus(ApplicationStatus.PENDING);
        List<Application> accepted = applicationRepository.findByStatus(ApplicationStatus.ACCEPTED);

        // Assert
        assertThat(pending).hasSize(1);
        assertThat(pending.get(0).getStatus()).isEqualTo(ApplicationStatus.PENDING);
        assertThat(accepted).hasSize(1);
        assertThat(accepted.get(0).getStatus()).isEqualTo(ApplicationStatus.ACCEPTED);
    }

    @Test
    void findByVolunteerIdAndStatus_ShouldReturnFilteredApplications() {
        // Arrange
        Application app1 = new Application(volunteer, opportunity);
        applicationRepository.save(app1);

        Opportunity opp2 = new Opportunity("Food Bank", "promoter@ua.pt", "Help", 15, "Porto");
        opp2 = entityManager.persist(opp2);
        Application app2 = new Application(volunteer, opp2);
        app2.accept();
        applicationRepository.save(app2);

        // Act
        List<Application> pending = applicationRepository.findByVolunteerIdAndStatus(
                volunteer.getId(), ApplicationStatus.PENDING);
        List<Application> accepted = applicationRepository.findByVolunteerIdAndStatus(
                volunteer.getId(), ApplicationStatus.ACCEPTED);

        // Assert
        assertThat(pending).hasSize(1);
        assertThat(accepted).hasSize(1);
    }

    @Test
    void countByOpportunityId_ShouldReturnCount() {
        // Arrange
        Application app1 = new Application(volunteer, opportunity);
        applicationRepository.save(app1);

        Volunteer vol2 = new Volunteer("Jane Doe", "jane@ua.pt", "password456");
        vol2 = entityManager.persist(vol2);
        Application app2 = new Application(vol2, opportunity);
        applicationRepository.save(app2);

        // Act
        long count = applicationRepository.countByOpportunityId(opportunity.getId());

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    void countByVolunteerId_ShouldReturnCount() {
        // Arrange
        Application app1 = new Application(volunteer, opportunity);
        applicationRepository.save(app1);

        Opportunity opp2 = new Opportunity("Food Bank", "promoter@ua.pt", "Help", 15, "Porto");
        opp2 = entityManager.persist(opp2);
        Application app2 = new Application(volunteer, opp2);
        applicationRepository.save(app2);

        // Act
        long count = applicationRepository.countByVolunteerId(volunteer.getId());

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    void existsByVolunteerIdAndOpportunityId_ShouldReturnTrue_WhenExists() {
        // Arrange
        Application application = new Application(volunteer, opportunity);
        applicationRepository.save(application);

        // Act
        boolean exists = applicationRepository.existsByVolunteerIdAndOpportunityId(
                volunteer.getId(), opportunity.getId());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsByVolunteerIdAndOpportunityId_ShouldReturnFalse_WhenNotExists() {
        // Act
        boolean exists = applicationRepository.existsByVolunteerIdAndOpportunityId(
                volunteer.getId(), opportunity.getId());

        // Assert
        assertThat(exists).isFalse();
    }
}
