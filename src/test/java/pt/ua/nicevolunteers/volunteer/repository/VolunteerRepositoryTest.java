package pt.ua.nicevolunteers.volunteer.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ua.nicevolunteers.volunteer.domain.Volunteer;

@DataJpaTest
class VolunteerRepositoryTest {

    @Autowired
    private VolunteerRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveAndLoadVolunteer() {
        // given
        Volunteer volunteer = new Volunteer(
                "Ana Silva",
                "ana.silva@ua.pt",
                "StrongPass123!"
        );

        // when
        repository.save(volunteer);

        // then
        var all = repository.findAll();
        assertThat(all).hasSize(1);

        Volunteer loaded = all.get(0);
        assertThat(loaded.getName()).isEqualTo("Ana Silva");
        assertThat(loaded.getEmail()).isEqualTo("ana.silva@ua.pt");
        assertThat(loaded.getHashedPassword()).isNotNull();
    }
}
