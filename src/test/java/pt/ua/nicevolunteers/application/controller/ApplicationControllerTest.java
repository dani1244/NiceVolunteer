package pt.ua.nicevolunteers.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.nicevolunteers.config.TestSecurityConfig;
import pt.ua.nicevolunteers.application.domain.Application;
import pt.ua.nicevolunteers.application.dto.CreateApplicationRequest;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.service.ApplicationService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
@Import(TestSecurityConfig.class)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplicationService applicationService;

    private Application application;
    private Volunteer volunteer;
    private Opportunity opportunity;
    private UUID volunteerId;
    private UUID opportunityId;
    private UUID applicationId;

    @BeforeEach
    void setUp() {
        volunteerId = UUID.randomUUID();
        opportunityId = UUID.randomUUID();
        applicationId = UUID.randomUUID();

        volunteer = new Volunteer("John Doe", "john@ua.pt", "password123");
        opportunity = new Opportunity("Beach Cleanup", "promoter@ua.pt", "Clean beach", 10, "Aveiro");
        application = new Application(volunteer, opportunity);
    }

    @Test
    void createApplication_WithValidData_ShouldReturn201() throws Exception {
        // Arrange
        CreateApplicationRequest request = new CreateApplicationRequest(volunteerId, opportunityId);
        when(applicationService.apply(volunteerId, opportunityId)).thenReturn(application);

        // Act & Assert
        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.volunteerName", is("John Doe")))
                .andExpect(jsonPath("$.opportunityTitle", is("Beach Cleanup")))
                .andExpect(jsonPath("$.status", is("PENDING")));

        verify(applicationService).apply(volunteerId, opportunityId);
    }

    @Test
    void getVolunteerApplications_ShouldReturnApplications() throws Exception {
        // Arrange
        when(applicationService.getVolunteerApplications(volunteerId))
                .thenReturn(List.of(application));

        // Act & Assert
        mockMvc.perform(get("/api/applications/volunteer/{volunteerId}", volunteerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].volunteerName", is("John Doe")))
                .andExpect(jsonPath("$[0].status", is("PENDING")));
    }

    @Test
    void getOpportunityApplications_ShouldReturnApplications() throws Exception {
        // Arrange
        when(applicationService.getOpportunityApplications(opportunityId))
                .thenReturn(List.of(application));

        // Act & Assert
        mockMvc.perform(get("/api/applications/opportunity/{opportunityId}", opportunityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].opportunityTitle", is("Beach Cleanup")));
    }

    @Test
    void getPendingApplications_ShouldReturnOnlyPending() throws Exception {
        // Arrange
        when(applicationService.getPendingApplicationsForOpportunity(opportunityId))
                .thenReturn(List.of(application));

        // Act & Assert
        mockMvc.perform(get("/api/applications/opportunity/{opportunityId}/pending", opportunityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("PENDING")));
    }

    @Test
    void acceptApplication_WithValidId_ShouldReturn200() throws Exception {
        // Arrange
        application.accept();
        when(applicationService.acceptApplication(applicationId)).thenReturn(application);

        // Act & Assert
        mockMvc.perform(post("/api/applications/{applicationId}/accept", applicationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ACCEPTED")))
                .andExpect(jsonPath("$.respondedAt").exists());

        verify(applicationService).acceptApplication(applicationId);
    }

    @Test
    void rejectApplication_WithValidId_ShouldReturn200() throws Exception {
        // Arrange
        application.reject();
        when(applicationService.rejectApplication(applicationId)).thenReturn(application);

        // Act & Assert
        mockMvc.perform(post("/api/applications/{applicationId}/reject", applicationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("REJECTED")))
                .andExpect(jsonPath("$.respondedAt").exists());

        verify(applicationService).rejectApplication(applicationId);
    }

    @Test
    void completeApplication_WithValidId_ShouldReturn200() throws Exception {
        // Arrange
        application.accept();
        application.complete();
        when(applicationService.completeApplication(applicationId)).thenReturn(application);

        // Act & Assert
        mockMvc.perform(post("/api/applications/{applicationId}/complete", applicationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("COMPLETED")));

        verify(applicationService).completeApplication(applicationId);
    }
}
