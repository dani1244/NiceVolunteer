package pt.ua.nicevolunteers.volunteer.repository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;

@SpringBootTest
class VolunteerRepositoryTest {

    @Autowired
    private VolunteerRepository repository;

    @Test
    void shouldSaveAndLoadVolunteer() {
        Volunteer volunteer = new Volunteer(
            "Ana Silva",
            "ana.silva@ua.pt",
            "StrongPass123!"
        );

        Volunteer saved = repository.save(volunteer);
        Optional<Volunteer> loaded = repository.findById(saved.getId());

        assertThat(loaded).isPresent();
        assertThat(loaded.get().getEmail()).isEqualTo("ana.silva@ua.pt");
    }
}
