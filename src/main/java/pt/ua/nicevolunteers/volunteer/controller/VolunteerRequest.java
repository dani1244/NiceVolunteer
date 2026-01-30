package pt.ua.nicevolunteers.volunteer.controller;

import java.util.List;

public record VolunteerRequest(
        String name,
        String email,
        String password,
        String bio,
        List<String> skills,
        List<String> interests
) { }
