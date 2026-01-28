package pt.ua.nicevolunteers.volunteer.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;
import pt.ua.nicevolunteers.volunteer.domain.points.PointsTransaction;
import pt.ua.nicevolunteers.volunteer.repository.VolunteerRepository;
import pt.ua.nicevolunteers.volunteer.repository.PointsTransactionRepository;

@SpringBootTest
@Transactional
class PointsServiceTest {

    @Autowired
    private PointsService service;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private PointsTransactionRepository transactionRepository;

    @Test
    void shouldReturnCurrentPointsBalance() {
        Volunteer v = volunteerRepository.save(
                new Volunteer("Ana", "ana@ua.pt", "StrongPass123!")
        );

        transactionRepository.save(new PointsTransaction(v, 20, "Activity A"));
        transactionRepository.save(new PointsTransaction(v, 30, "Activity B"));

        int balance = service.getCurrentBalance(v.getId());

        assertEquals(50, balance);
    }

    @Test
    void shouldReturnPointsHistoryOrderedByDate() {
        Volunteer v = volunteerRepository.save(
                new Volunteer("Rui", "rui@ua.pt", "StrongPass123!")
        );

        transactionRepository.save(new PointsTransaction(v, 10, "Task 1"));
        transactionRepository.save(new PointsTransaction(v, 25, "Task 2"));

        List<PointsTransaction> history = service.getHistory(v.getId());

        assertEquals(2, history.size());
        assertTrue(history.get(0).getTimestamp().isBefore(history.get(1).getTimestamp()));
    }

    @Test
    void shouldRejectAccessToAnotherVolunteersHistory() {
        Volunteer v1 = volunteerRepository.save(
                new Volunteer("Ana", "ana@ua.pt", "StrongPass123!")
        );

        Volunteer v2 = volunteerRepository.save(
                new Volunteer("Pedro", "pedro@ua.pt", "StrongPass123!")
        );

        transactionRepository.save(new PointsTransaction(v1, 40, "Private Task"));

        assertThrows(SecurityException.class, () ->
                service.getHistoryForVolunteer(v2.getId(), v1.getId())
        );
    }
}
