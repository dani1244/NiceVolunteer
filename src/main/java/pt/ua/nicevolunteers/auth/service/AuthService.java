package pt.ua.nicevolunteers.auth.service;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.auth.exception.InvalidCredentialsException;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@Service
public class AuthService {

    private final VolunteerRepository volunteerRepository;

    public AuthService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public void login(String email, String rawPassword) {

        Volunteer volunteer = volunteerRepository
                .findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        String hashedInput = Integer.toHexString(rawPassword.hashCode());

        if (!volunteer.getHashedPassword().equals(hashedInput)) {
            throw new InvalidCredentialsException();
        }
    }
}
