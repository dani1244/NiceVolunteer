package pt.ua.nicevolunteers.volunteer.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidEmailException;

class EmailTest {

    @Test
    void shouldAcceptInstitutionalEmail() {
        Email email = new Email("ana.silva@ua.pt");
        assertEquals("ana.silva@ua.pt", email.getValue());
    }

    @Test
    void shouldRejectNonInstitutionalEmail() {
        assertThrows(InvalidEmailException.class, () -> {
            new Email("ana@gmail.com");
        });
    }

    @Test
    void shouldRejectNullEmail() {
        assertThrows(InvalidEmailException.class, () -> {
            new Email(null);
        });
    }
}
