package pt.ua.nicevolunteers.volunteer.service;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;

@Service
public class VolunteerService {

    private final VolunteerRepository repository;

    public VolunteerService(VolunteerRepository repository) {
        this.repository = repository;
    }

    public Volunteer register(String name, String email, String password) {
        Volunteer volunteer = new Volunteer(name, email, password);
        return repository.save(volunteer);
    }
}
