package pt.ua.nicevolunteers.promoter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.nicevolunteers.auth.exception.AccountNotFoundException;
import pt.ua.nicevolunteers.promoter.domain.Promoter;
import pt.ua.nicevolunteers.promoter.repository.PromoterRepository;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidEmailException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromoterServiceTest {

    @Mock
    private PromoterRepository promoterRepository;

    @InjectMocks
    private PromoterService promoterService;

    private Promoter promoter;
    private UUID promoterId;

    @BeforeEach
    void setUp() {
        promoterId = UUID.randomUUID();
        promoter = new Promoter("UA Foundation", "foundation@ua.pt", "password123");
    }

    @Test
    void register_WithValidData_ShouldCreatePromoter() {
        // Arrange
        when(promoterRepository.existsByEmail("foundation@ua.pt")).thenReturn(false);
        when(promoterRepository.save(any(Promoter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Promoter result = promoterService.register("UA Foundation", "foundation@ua.pt", "password123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("UA Foundation");
        assertThat(result.getEmail()).isEqualTo("foundation@ua.pt");
        verify(promoterRepository).save(any(Promoter.class));
    }

    @Test
    void register_WithExistingEmail_ShouldThrowException() {
        // Arrange
        when(promoterRepository.existsByEmail("foundation@ua.pt")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> promoterService.register("UA Foundation", "foundation@ua.pt", "password123"))
                .isInstanceOf(InvalidEmailException.class);
        verify(promoterRepository, never()).save(any());
    }

    @Test
    void register_WithInvalidEmail_ShouldThrowException() {
        // Arrange
        when(promoterRepository.existsByEmail("foundation@gmail.com")).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> promoterService.register("UA Foundation", "foundation@gmail.com", "password123"))
                .isInstanceOf(InvalidEmailException.class);
        verify(promoterRepository, never()).save(any());
    }

    @Test
    void getById_WithExistingId_ShouldReturnPromoter() {
        // Arrange
        when(promoterRepository.findById(promoterId)).thenReturn(Optional.of(promoter));

        // Act
        Promoter result = promoterService.getById(promoterId);

        // Assert
        assertThat(result).isEqualTo(promoter);
    }

    @Test
    void getById_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(promoterRepository.findById(promoterId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> promoterService.getById(promoterId))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getByEmail_WithExistingEmail_ShouldReturnPromoter() {
        // Arrange
        when(promoterRepository.findByEmail("foundation@ua.pt")).thenReturn(Optional.of(promoter));

        // Act
        Promoter result = promoterService.getByEmail("foundation@ua.pt");

        // Assert
        assertThat(result).isEqualTo(promoter);
    }

    @Test
    void getByEmail_WithNonExistingEmail_ShouldThrowException() {
        // Arrange
        when(promoterRepository.findByEmail("notfound@ua.pt")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> promoterService.getByEmail("notfound@ua.pt"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getAllPromoters_ShouldReturnAllPromoters() {
        // Arrange
        Promoter promoter2 = new Promoter("DETI", "deti@ua.pt", "password456");
        when(promoterRepository.findAll()).thenReturn(List.of(promoter, promoter2));

        // Act
        List<Promoter> result = promoterService.getAllPromoters();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(promoter, promoter2);
    }

    @Test
    void updateProfile_WithValidData_ShouldUpdatePromoter() {
        // Arrange
        when(promoterRepository.findById(promoterId)).thenReturn(Optional.of(promoter));
        when(promoterRepository.save(any(Promoter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Promoter result = promoterService.updateProfile(promoterId, "New description", "https://ua.pt", "234567890");

        // Assert
        assertThat(result.getDescription()).isEqualTo("New description");
        assertThat(result.getWebsite()).isEqualTo("https://ua.pt");
        assertThat(result.getContactPhone()).isEqualTo("234567890");
        verify(promoterRepository).save(promoter);
    }

    @Test
    void verifyCredentials_WithCorrectPassword_ShouldReturnTrue() {
        // Arrange
        when(promoterRepository.findByEmail("foundation@ua.pt")).thenReturn(Optional.of(promoter));

        // Act
        boolean result = promoterService.verifyCredentials("foundation@ua.pt", "password123");

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void verifyCredentials_WithIncorrectPassword_ShouldReturnFalse() {
        // Arrange
        when(promoterRepository.findByEmail("foundation@ua.pt")).thenReturn(Optional.of(promoter));

        // Act
        boolean result = promoterService.verifyCredentials("foundation@ua.pt", "wrongpassword");

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void updatePassword_WithValidData_ShouldUpdatePassword() {
        // Arrange
        when(promoterRepository.findById(promoterId)).thenReturn(Optional.of(promoter));
        when(promoterRepository.save(any(Promoter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        promoterService.updatePassword(promoterId, "newpassword");

        // Assert
        verify(promoterRepository).save(promoter);
    }
}
