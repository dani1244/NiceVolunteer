package pt.ua.nicevolunteers.volunteer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidEmailException;
import pt.ua.nicevolunteers.volunteer.domain.exception.WeakPasswordException;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@Service
public class VolunteerService {

    private final VolunteerRepository repository;

    public VolunteerService(VolunteerRepository repository) {
        this.repository = repository;
    }

    public Volunteer register(String name, String email, String rawPassword,
                              String bio, List<String> skills, List<String> interests) {

        if (!email.endsWith("@ua.pt")) {
            throw new InvalidEmailException();
        }

        if (rawPassword.length() < 8) {
            throw new WeakPasswordException();
        }

        String hashed = Integer.toHexString(rawPassword.hashCode());
        Volunteer v = new Volunteer(name, email, hashed);

        if (bio != null) {
            v.setBio(bio);
        }
        if (skills != null) {
            v.setSkills(skills);
        }
        if (interests != null) {
            v.setInterests(interests);
        }

        return repository.save(v);
    }
}
