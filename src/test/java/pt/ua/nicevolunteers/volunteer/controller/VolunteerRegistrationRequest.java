package pt.ua.nicevolunteers.volunteer.controller;

public record VolunteerRegistrationRequest(
        String name,
        String email,
        String password
) {}
