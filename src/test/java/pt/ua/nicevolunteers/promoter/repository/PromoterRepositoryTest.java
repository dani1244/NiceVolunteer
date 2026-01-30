package pt.ua.nicevolunteers.promoter.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ua.nicevolunteers.promoter.domain.Promoter;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PromoterRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PromoterRepository promoterRepository;

    private Promoter promoter;

    @BeforeEach
    void setUp() {
        promoter = new Promoter("UA Foundation", "foundation@ua.pt", "password123");
        promoter = promoterRepository.save(promoter);
    }

    @Test
    void findByEmail_ShouldReturnPromoter_WhenExists() {
        // Act
        Optional<Promoter> result = promoterRepository.findByEmail("foundation@ua.pt");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("UA Foundation");
        assertThat(result.get().getEmail()).isEqualTo("foundation@ua.pt");
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenNotExists() {
        // Act
        Optional<Promoter> result = promoterRepository.findByEmail("notfound@ua.pt");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenExists() {
        // Act
        boolean exists = promoterRepository.existsByEmail("foundation@ua.pt");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenNotExists() {
        // Act
        boolean exists = promoterRepository.existsByEmail("notfound@ua.pt");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistPromoter() {
        // Arrange
        Promoter newPromoter = new Promoter("DETI", "deti@ua.pt", "password456");

        // Act
        Promoter saved = promoterRepository.save(newPromoter);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("DETI");
        assertThat(saved.getEmail()).isEqualTo("deti@ua.pt");
    }

    @Test
    void findAll_ShouldReturnAllPromoters() {
        // Arrange
        Promoter promoter2 = new Promoter("DETI", "deti@ua.pt", "password456");
        promoterRepository.save(promoter2);

        // Act
        var promoters = promoterRepository.findAll();

        // Assert
        assertThat(promoters).hasSize(2);
        assertThat(promoters).extracting(Promoter::getEmail)
                .containsExactlyInAnyOrder("foundation@ua.pt", "deti@ua.pt");
    }

    @Test
    void updatePromoter_ShouldPersistChanges() {
        // Act
        promoter.updateProfile("Updated description", "https://ua.pt", "123456789");
        promoterRepository.save(promoter);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Promoter updated = promoterRepository.findById(promoter.getId()).orElseThrow();
        assertThat(updated.getDescription()).isEqualTo("Updated description");
        assertThat(updated.getWebsite()).isEqualTo("https://ua.pt");
        assertThat(updated.getContactPhone()).isEqualTo("123456789");
    }
}
