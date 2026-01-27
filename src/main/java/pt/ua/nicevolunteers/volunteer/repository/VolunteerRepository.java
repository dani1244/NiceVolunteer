package pt.ua.nicevolunteers.volunteer.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, UUID> {
}

