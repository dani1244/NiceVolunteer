package pt.ua.nicevolunteers.volunteer.controller;

public record VolunteerRequest(
        String name,
        String email,
        String password
) {}
