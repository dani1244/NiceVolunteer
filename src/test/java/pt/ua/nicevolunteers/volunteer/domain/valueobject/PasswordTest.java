package pt.ua.nicevolunteers.volunteer.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import pt.ua.nicevolunteers.volunteer.domain.exception.WeakPasswordException;

class PasswordTest {

    @Test
    void shouldCreatePasswordWhenStrong() {
        Password password = new Password("StrongPass123!");
        assertNotNull(password.getHashedValue());
    }

    @Test
    void shouldRejectShortPassword() {
        assertThrows(WeakPasswordException.class, () -> {
            new Password("123");
        });
    }
}
