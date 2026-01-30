package pt.ua.nicevolunteers.volunteer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import pt.ua.nicevolunteers.volunteer.service.VolunteerService;
import pt.ua.nicevolunteers.volunteer.domain.Volunteer;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerService service;

    public VolunteerController(VolunteerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Volunteer> register(@RequestBody VolunteerRequest request) {
        Volunteer volunteer = service.register(
                request.name(),
                request.email(),
                request.password()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(volunteer);
    }

    @PostMapping("/register")
    public ResponseEntity<Volunteer> registerAlias(@RequestBody VolunteerRequest request) {
        return register(request);
    }
}
