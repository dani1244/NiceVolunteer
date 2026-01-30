package pt.ua.nicevolunteers.promoter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ua.nicevolunteers.auth.exception.AccountNotFoundException;
import pt.ua.nicevolunteers.promoter.domain.Promoter;
import pt.ua.nicevolunteers.promoter.repository.PromoterRepository;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidEmailException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PromoterService {

    private final PromoterRepository promoterRepository;

    public PromoterService(PromoterRepository promoterRepository) {
        this.promoterRepository = promoterRepository;
    }

    public Promoter register(String name, String email, String rawPassword) {
        if (promoterRepository.existsByEmail(email)) {
            throw new InvalidEmailException();
        }

        Promoter promoter = new Promoter(name, email, rawPassword);
        return promoterRepository.save(promoter);
    }

    public Promoter getById(UUID id) {
        return promoterRepository.findById(id)
                .orElseThrow(AccountNotFoundException::new);
    }

    public Promoter getByEmail(String email) {
        return promoterRepository.findByEmail(email)
                .orElseThrow(AccountNotFoundException::new);
    }

    public List<Promoter> getAllPromoters() {
        return promoterRepository.findAll();
    }

    public Promoter updateProfile(UUID promoterId, String description, String website, String contactPhone) {
        Promoter promoter = getById(promoterId);
        promoter.updateProfile(description, website, contactPhone);
        return promoterRepository.save(promoter);
    }

    public void updatePassword(UUID promoterId, String newRawPassword) {
        Promoter promoter = getById(promoterId);
        promoter.updatePassword(newRawPassword);
        promoterRepository.save(promoter);
    }

    public boolean verifyCredentials(String email, String rawPassword) {
        Promoter promoter = promoterRepository.findByEmail(email)
                .orElseThrow(AccountNotFoundException::new);
        return promoter.verifyPassword(rawPassword);
    }
}
