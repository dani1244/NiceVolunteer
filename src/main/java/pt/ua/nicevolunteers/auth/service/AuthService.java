package pt.ua.nicevolunteers.auth.service;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.auth.exception.InvalidCredentialsException;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final VolunteerRepository volunteerRepository;

    public AuthService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public Map<String, Object> login(String email, String rawPassword) {

        Volunteer volunteer = volunteerRepository
                .findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        String hashedInput = Integer.toHexString(rawPassword.hashCode());

        if (!volunteer.getHashedPassword().equals(hashedInput)) {
            throw new InvalidCredentialsException();
        }

        // Generate a simple token (in production, use JWT)
        String token = "Bearer-" + UUID.randomUUID().toString();

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("volunteer", volunteer);

        return response;
    }
}
