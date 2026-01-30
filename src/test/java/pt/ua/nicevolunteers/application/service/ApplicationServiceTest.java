package pt.ua.nicevolunteers.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.nicevolunteers.application.domain.Application;
import pt.ua.nicevolunteers.application.domain.ApplicationStatus;
import pt.ua.nicevolunteers.application.repository.ApplicationRepository;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidApplicationException;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;
import pt.ua.nicevolunteers.volunteer.service.ApplicationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private OpportunityRepository opportunityRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private Volunteer volunteer;
    private Opportunity opportunity;
    private UUID volunteerId;
    private UUID opportunityId;

    @BeforeEach
    void setUp() {
        volunteerId = UUID.randomUUID();
        opportunityId = UUID.randomUUID();

        volunteer = new Volunteer("John Doe", "john@ua.pt", "password123");
        opportunity = new Opportunity("Clean Beach", "promoter@ua.pt", "Beach cleanup", 10, "Aveiro");
    }

    @Test
    void apply_WithValidData_ShouldCreateApplication() {
        // Arrange
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(opportunityRepository.findById(opportunityId)).thenReturn(Optional.of(opportunity));
        when(applicationRepository.existsByVolunteerIdAndOpportunityId(volunteerId, opportunityId)).thenReturn(false);
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Application result = applicationService.apply(volunteerId, opportunityId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getVolunteer()).isEqualTo(volunteer);
        assertThat(result.getOpportunity()).isEqualTo(opportunity);
        assertThat(result.getStatus()).isEqualTo(ApplicationStatus.PENDING);
        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    void apply_WithNonExistentVolunteer_ShouldThrowException() {
        // Arrange
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> applicationService.apply(volunteerId, opportunityId))
                .isInstanceOf(InvalidApplicationException.class);
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void apply_WithNonExistentOpportunity_ShouldThrowException() {
        // Arrange
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(opportunityRepository.findById(opportunityId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> applicationService.apply(volunteerId, opportunityId))
                .isInstanceOf(InvalidApplicationException.class);
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void apply_WithClosedOpportunity_ShouldThrowException() {
        // Arrange
        opportunity.close();
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(opportunityRepository.findById(opportunityId)).thenReturn(Optional.of(opportunity));

        // Act & Assert
        assertThatThrownBy(() -> applicationService.apply(volunteerId, opportunityId))
                .isInstanceOf(InvalidApplicationException.class);
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void apply_WithDuplicateApplication_ShouldThrowException() {
        // Arrange
        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(opportunityRepository.findById(opportunityId)).thenReturn(Optional.of(opportunity));
        when(applicationRepository.existsByVolunteerIdAndOpportunityId(volunteerId, opportunityId)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> applicationService.apply(volunteerId, opportunityId))
                .isInstanceOf(InvalidApplicationException.class);
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void getVolunteerApplications_WithValidVolunteerId_ShouldReturnApplications() {
        // Arrange
        Application app1 = new Application(volunteer, opportunity);
        when(volunteerRepository.existsById(volunteerId)).thenReturn(true);
        when(applicationRepository.findByVolunteerId(volunteerId)).thenReturn(List.of(app1));

        // Act
        List<Application> result = applicationService.getVolunteerApplications(volunteerId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(app1);
    }

    @Test
    void getVolunteerApplications_WithInvalidVolunteerId_ShouldThrowException() {
        // Arrange
        when(volunteerRepository.existsById(volunteerId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> applicationService.getVolunteerApplications(volunteerId))
                .isInstanceOf(InvalidApplicationException.class);
    }

    @Test
    void acceptApplication_WithValidApplication_ShouldAcceptIt() {
        // Arrange
        Application application = new Application(volunteer, opportunity);
        UUID applicationId = application.getId();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Application result = applicationService.acceptApplication(applicationId);

        // Assert
        assertThat(result.getStatus()).isEqualTo(ApplicationStatus.ACCEPTED);
        assertThat(result.getRespondedAt()).isNotNull();
        verify(applicationRepository).save(application);
    }

    @Test
    void rejectApplication_WithValidApplication_ShouldRejectIt() {
        // Arrange
        Application application = new Application(volunteer, opportunity);
        UUID applicationId = application.getId();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Application result = applicationService.rejectApplication(applicationId);

        // Assert
        assertThat(result.getStatus()).isEqualTo(ApplicationStatus.REJECTED);
        assertThat(result.getRespondedAt()).isNotNull();
        verify(applicationRepository).save(application);
    }

    @Test
    void completeApplication_WithAcceptedApplication_ShouldCompleteIt() {
        // Arrange
        Application application = new Application(volunteer, opportunity);
        application.accept();
        UUID applicationId = application.getId();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Application result = applicationService.completeApplication(applicationId);

        // Assert
        assertThat(result.getStatus()).isEqualTo(ApplicationStatus.COMPLETED);
        verify(applicationRepository).save(application);
    }

    @Test
    void getPendingApplicationsForOpportunity_ShouldReturnPendingOnly() {
        // Arrange
        Application pendingApp = new Application(volunteer, opportunity);
        when(applicationRepository.findByOpportunityIdAndStatus(opportunityId, ApplicationStatus.PENDING))
                .thenReturn(List.of(pendingApp));

        // Act
        List<Application> result = applicationService.getPendingApplicationsForOpportunity(opportunityId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(ApplicationStatus.PENDING);
    }
}
