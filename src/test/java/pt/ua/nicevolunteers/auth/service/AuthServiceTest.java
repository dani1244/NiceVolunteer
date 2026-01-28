package pt.ua.nicevolunteers.auth.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pt.ua.nicevolunteers.auth.exception.InvalidCredentialsException;
import pt.ua.nicevolunteers.volunteer.service.VolunteerService;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private VolunteerService volunteerService;

    @Test
    void login_success() {
        // given
        String email = "user1@ua.pt";
        String password = "Password123";
        volunteerService.register("User Test", email, password);

        // when / then
        assertDoesNotThrow(() -> authService.login(email, password));
    }

    @Test
    void login_invalid_password() {
        // given
        String email = "user2@ua.pt";
        String password = "Password123";
        volunteerService.register("User Test", email, password);

        // when / then
        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(email, "WrongPassword"));
    }

    @Test
    void login_unknown_email() {
        assertThrows(InvalidCredentialsException.class,
                () -> authService.login("ghost@ua.pt", "whatever"));
    }
}
