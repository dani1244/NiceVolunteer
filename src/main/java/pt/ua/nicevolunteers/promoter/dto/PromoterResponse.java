package pt.ua.nicevolunteers.promoter.dto;

import pt.ua.nicevolunteers.promoter.domain.Promoter;

import java.util.UUID;

public record PromoterResponse(
        UUID id,
        String name,
        String email,
        String description,
        String website,
        String contactPhone
) {
    public static PromoterResponse fromEntity(Promoter promoter) {
        return new PromoterResponse(
                promoter.getId(),
                promoter.getName(),
                promoter.getEmail(),
                promoter.getDescription(),
                promoter.getWebsite(),
                promoter.getContactPhone()
        );
    }
}
