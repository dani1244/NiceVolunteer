package pt.ua.nicevolunteers.volunteer.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import pt.ua.nicevolunteers.volunteer.domain.points.PointsTransaction;
import pt.ua.nicevolunteers.volunteer.repository.PointsTransactionRepository;

@Service
public class PointsService {

    private final PointsTransactionRepository repository;

    public PointsService(PointsTransactionRepository repository) {
        this.repository = repository;
    }

    public int getCurrentBalance(UUID volunteerId) {
        return repository.findByVolunteerIdOrderByTimestampAsc(volunteerId)
                .stream()
                .mapToInt(PointsTransaction::getPoints)
                .sum();
    }

    public List<PointsTransaction> getHistory(UUID volunteerId) {
        return repository.findByVolunteerIdOrderByTimestampAsc(volunteerId);
    }

    public List<PointsTransaction> getHistoryForVolunteer(UUID requesterId, UUID targetVolunteerId) {
        if (!requesterId.equals(targetVolunteerId)) {
            throw new SecurityException("Cannot access another volunteer's points history");
        }
        return getHistory(targetVolunteerId);
    }
}
