package pt.ua.nicevolunteers.volunteer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ua.nicevolunteers.config.TestSecurityConfig;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterVolunteer() throws Exception {
        String json = """
            {
                "name": "Ana Silva",
                "email": "ana.silva@ua.pt",
                "password": "StrongPass123!"
            }
        """;

        mockMvc.perform(post("/api/volunteers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }
}
