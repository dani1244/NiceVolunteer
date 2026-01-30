package pt.ua.nicevolunteers.promoter.controller;

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
import pt.ua.nicevolunteers.promoter.domain.Promoter;
import pt.ua.nicevolunteers.promoter.dto.CreatePromoterRequest;
import pt.ua.nicevolunteers.promoter.service.PromoterService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromoterController.class)
@Import(TestSecurityConfig.class)
class PromoterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PromoterService promoterService;

    private Promoter promoter;
    private UUID promoterId;

    @BeforeEach
    void setUp() {
        promoterId = UUID.randomUUID();
        promoter = new Promoter("UA Foundation", "foundation@ua.pt", "password123");
    }

    @Test
    void registerPromoter_WithValidData_ShouldReturn201() throws Exception {
        // Arrange
        CreatePromoterRequest request = new CreatePromoterRequest("UA Foundation", "foundation@ua.pt", "password123");
        when(promoterService.register(anyString(), anyString(), anyString())).thenReturn(promoter);

        // Act & Assert
        mockMvc.perform(post("/api/promoters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("UA Foundation")))
                .andExpect(jsonPath("$.email", is("foundation@ua.pt")));
    }

    @Test
    void getAllPromoters_ShouldReturnList() throws Exception {
        // Arrange
        Promoter promoter2 = new Promoter("DETI", "deti@ua.pt", "password456");
        when(promoterService.getAllPromoters()).thenReturn(List.of(promoter, promoter2));

        // Act & Assert
        mockMvc.perform(get("/api/promoters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("UA Foundation")))
                .andExpect(jsonPath("$[1].name", is("DETI")));
    }

    @Test
    void getPromoterById_ShouldReturnPromoter() throws Exception {
        // Arrange
        when(promoterService.getById(promoterId)).thenReturn(promoter);

        // Act & Assert
        mockMvc.perform(get("/api/promoters/{id}", promoterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UA Foundation")))
                .andExpect(jsonPath("$.email", is("foundation@ua.pt")));
    }

    @Test
    void getPromoterByEmail_ShouldReturnPromoter() throws Exception {
        // Arrange
        when(promoterService.getByEmail("foundation@ua.pt")).thenReturn(promoter);

        // Act & Assert
        mockMvc.perform(get("/api/promoters/email/{email}", "foundation@ua.pt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UA Foundation")))
                .andExpect(jsonPath("$.email", is("foundation@ua.pt")));
    }

    @Test
    void updatePromoter_ShouldReturnUpdatedPromoter() throws Exception {
        // Arrange
        promoter.updateProfile("New description", "https://ua.pt", "123456789");
        when(promoterService.updateProfile(promoterId, "New description", "https://ua.pt", "123456789"))
                .thenReturn(promoter);

        // Act & Assert
        mockMvc.perform(put("/api/promoters/{id}", promoterId)
                        .param("description", "New description")
                        .param("website", "https://ua.pt")
                        .param("contactPhone", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("New description")))
                .andExpect(jsonPath("$.website", is("https://ua.pt")))
                .andExpect(jsonPath("$.contactPhone", is("123456789")));
    }
}
