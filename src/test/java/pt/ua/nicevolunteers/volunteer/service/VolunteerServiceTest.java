package pt.ua.nicevolunteers.volunteer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidEmailException;
import pt.ua.nicevolunteers.volunteer.domain.exception.WeakPasswordException;

@SpringBootTest
@Transactional
class VolunteerServiceTest {

    @Autowired
    private VolunteerService service;

    @Test
    void shouldRegisterVolunteerWithValidInstitutionalEmail() {
        Volunteer v = service.register(
                "Ana Silva",
                "ana.silva@ua.pt",
                "StrongPass123!"
        );

        assertNotNull(v.getId());
        assertEquals("ana.silva@ua.pt", v.getEmail());
    }

    @Test
    void shouldRejectVolunteerWithNonInstitutionalEmail() {
        assertThrows(InvalidEmailException.class, () ->
                service.register("Ana", "ana@gmail.com", "StrongPass123!")
        );
    }

    @Test
    void shouldRejectVolunteerWithWeakPassword() {
        assertThrows(WeakPasswordException.class, () ->
                service.register("Ana", "ana@ua.pt", "123")
        );
    }
}
