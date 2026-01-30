package pt.ua.nicevolunteers.points.controller;

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
import pt.ua.nicevolunteers.points.dto.CompleteActivityRequest;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.points.PointsTransaction;
import pt.ua.nicevolunteers.volunteer.service.ActivityCompletionService;
import pt.ua.nicevolunteers.volunteer.service.PointsService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointsController.class)
@Import(TestSecurityConfig.class)
class PointsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PointsService pointsService;

    @MockBean
    private ActivityCompletionService activityCompletionService;

    private UUID volunteerId;
    private UUID opportunityId;
    private Volunteer volunteer;

    @BeforeEach
    void setUp() {
        volunteerId = UUID.randomUUID();
        opportunityId = UUID.randomUUID();
        volunteer = new Volunteer("John Doe", "john@ua.pt", "password123");
    }

    @Test
    void getBalance_ShouldReturnCurrentBalance() throws Exception {
        // Arrange
        when(pointsService.getCurrentBalance(volunteerId)).thenReturn(50);

        // Act & Assert
        mockMvc.perform(get("/api/points/balance/{volunteerId}", volunteerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.volunteerId", is(volunteerId.toString())))
                .andExpect(jsonPath("$.currentBalance", is(50)));
    }

    @Test
    void getHistory_WithoutRequester_ShouldReturnHistory() throws Exception {
        // Arrange
        PointsTransaction transaction = new PointsTransaction(volunteer, 10, "Completed activity");
        when(pointsService.getHistory(volunteerId)).thenReturn(List.of(transaction));

        // Act & Assert
        mockMvc.perform(get("/api/points/history/{volunteerId}", volunteerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].points", is(10)))
                .andExpect(jsonPath("$[0].description", is("Completed activity")));
    }

    @Test
    void getHistory_WithRequester_ShouldReturnHistory() throws Exception {
        // Arrange
        UUID requesterId = UUID.randomUUID();
        PointsTransaction transaction = new PointsTransaction(volunteer, 10, "Completed activity");
        when(pointsService.getHistoryForVolunteer(requesterId, volunteerId)).thenReturn(List.of(transaction));

        // Act & Assert
        mockMvc.perform(get("/api/points/history/{volunteerId}", volunteerId)
                        .param("requesterId", requesterId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].points", is(10)));
    }

    @Test
    void completeActivity_ShouldReturnNewBalance() throws Exception {
        // Arrange
        CompleteActivityRequest request = new CompleteActivityRequest(opportunityId, volunteerId, "promoter@ua.pt");
        when(pointsService.getCurrentBalance(volunteerId)).thenReturn(25);
        doNothing().when(activityCompletionService).completeActivity(opportunityId, volunteerId, "promoter@ua.pt");

        // Act & Assert
        mockMvc.perform(post("/api/points/complete-activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.volunteerId", is(volunteerId.toString())))
                .andExpect(jsonPath("$.currentBalance", is(25)));

        verify(activityCompletionService).completeActivity(opportunityId, volunteerId, "promoter@ua.pt");
    }
}
