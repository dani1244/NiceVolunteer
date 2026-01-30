package pt.ua.nicevolunteers.promoter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ua.nicevolunteers.promoter.domain.Promoter;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromoterRepository extends JpaRepository<Promoter, UUID> {

    Optional<Promoter> findByEmail(String email);

    boolean existsByEmail(String email);
}
