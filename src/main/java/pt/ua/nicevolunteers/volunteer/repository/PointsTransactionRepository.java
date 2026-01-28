package pt.ua.nicevolunteers.volunteer.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ua.nicevolunteers.volunteer.domain.points.PointsTransaction;

public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, UUID> {

    List<PointsTransaction> findByVolunteerIdOrderByTimestampAsc(UUID volunteerId);
}
