package pt.ua.nicevolunteers.opportunity.controller;

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
import pt.ua.nicevolunteers.opportunity.dto.CreateOpportunityRequest;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.service.OpportunityService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OpportunityController.class)
@Import(TestSecurityConfig.class)
class OpportunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OpportunityService opportunityService;

    @MockBean
    private OpportunityRepository opportunityRepository;

    private Opportunity opportunity;
    private UUID opportunityId;

    @BeforeEach
    void setUp() {
        opportunityId = UUID.randomUUID();
        opportunity = new Opportunity("Beach Cleanup", "promoter@ua.pt", "Clean the beach", 10, "Aveiro");
    }

    @Test
    void createOpportunity_WithValidData_ShouldReturn201() throws Exception {
        // Arrange
        CreateOpportunityRequest request = new CreateOpportunityRequest(
                "Beach Cleanup", "promoter@ua.pt", "Clean the beach", 10, "Aveiro");
        when(opportunityService.createOpportunity(anyString(), anyString(), anyString(), anyInt(), anyString()))
                .thenReturn(opportunity);

        // Act & Assert
        mockMvc.perform(post("/api/opportunities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Beach Cleanup")))
                .andExpect(jsonPath("$.promoter", is("promoter@ua.pt")))
                .andExpect(jsonPath("$.points", is(10)));

        verify(opportunityService).createOpportunity("Beach Cleanup", "promoter@ua.pt", "Clean the beach", 10, "Aveiro");
    }

    @Test
    void getAllOpportunities_ShouldReturnAllOpportunities() throws Exception {
        // Arrange
        Opportunity opp2 = new Opportunity("Food Bank", "promoter@ua.pt", "Help at food bank", 15, "Porto");
        when(opportunityRepository.findAll()).thenReturn(List.of(opportunity, opp2));

        // Act & Assert
        mockMvc.perform(get("/api/opportunities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Beach Cleanup")))
                .andExpect(jsonPath("$[1].title", is("Food Bank")));
    }

    @Test
    void getOpportunityById_WithExistingId_ShouldReturnOpportunity() throws Exception {
        // Arrange
        when(opportunityRepository.findById(opportunityId)).thenReturn(Optional.of(opportunity));

        // Act & Assert
        mockMvc.perform(get("/api/opportunities/{id}", opportunityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Beach Cleanup")))
                .andExpect(jsonPath("$.description", is("Clean the beach")));
    }

    @Test
    void getOpportunityById_WithNonExistingId_ShouldReturn404() throws Exception {
        // Arrange
        when(opportunityRepository.findById(opportunityId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/opportunities/{id}", opportunityId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOpenOpportunities_ShouldReturnOnlyOpenOnes() throws Exception {
        // Arrange
        Opportunity closedOpp = new Opportunity("Closed Event", "promoter@ua.pt", "Already closed", 5, "Aveiro");
        closedOpp.close();
        when(opportunityService.getOpenOpportunities()).thenReturn(List.of(opportunity));

        // Act & Assert
        mockMvc.perform(get("/api/opportunities/open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Beach Cleanup")))
                .andExpect(jsonPath("$[0].status", is("OPEN")));
    }

    @Test
    void closeOpportunity_WithExistingId_ShouldCloseAndReturn200() throws Exception {
        // Arrange
        when(opportunityRepository.findById(opportunityId)).thenReturn(Optional.of(opportunity));
        doAnswer(invocation -> {
            opportunity.close();
            return null;
        }).when(opportunityService).closeOpportunity(any());

        // Act & Assert
        mockMvc.perform(post("/api/opportunities/{id}/close", opportunityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CLOSED")));

        verify(opportunityService).closeOpportunity(opportunity);
    }

    @Test
    void completeOpportunity_WithExistingId_ShouldCompleteAndReturn200() throws Exception {
        // Arrange
        when(opportunityRepository.findById(opportunityId)).thenReturn(Optional.of(opportunity));
        doAnswer(invocation -> {
            opportunity.complete();
            return null;
        }).when(opportunityService).completeOpportunity(any());

        // Act & Assert
        mockMvc.perform(post("/api/opportunities/{id}/complete", opportunityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("COMPLETED")));

        verify(opportunityService).completeOpportunity(opportunity);
    }

    @Test
    void deleteOpportunity_WithExistingId_ShouldReturn204() throws Exception {
        // Arrange
        when(opportunityRepository.existsById(opportunityId)).thenReturn(true);
        doNothing().when(opportunityRepository).deleteById(opportunityId);

        // Act & Assert
        mockMvc.perform(delete("/api/opportunities/{id}", opportunityId))
                .andExpect(status().isNoContent());

        verify(opportunityRepository).deleteById(opportunityId);
    }

    @Test
    void deleteOpportunity_WithNonExistingId_ShouldReturn404() throws Exception {
        // Arrange
        when(opportunityRepository.existsById(opportunityId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/opportunities/{id}", opportunityId))
                .andExpect(status().isNotFound());

        verify(opportunityRepository, never()).deleteById(any());
    }

    @Test
    void getOpportunitiesByStatus_ShouldFilterByStatus() throws Exception {
        // Arrange
        when(opportunityRepository.findByStatus(OpportunityStatus.OPEN))
                .thenReturn(List.of(opportunity));

        // Act & Assert
        mockMvc.perform(get("/api/opportunities/status/{status}", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("OPEN")));
    }
}
