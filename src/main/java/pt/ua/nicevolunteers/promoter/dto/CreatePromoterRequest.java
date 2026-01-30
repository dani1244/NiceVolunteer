package pt.ua.nicevolunteers.promoter.dto;

public record CreatePromoterRequest(
        String name,
        String email,
        String password
) {
}
