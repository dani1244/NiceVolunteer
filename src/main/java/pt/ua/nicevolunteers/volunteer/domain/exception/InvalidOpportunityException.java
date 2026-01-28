package pt.ua.nicevolunteers.volunteer.domain.exception;

public class InvalidOpportunityException extends RuntimeException {
    public InvalidOpportunityException(String message) {
        super(message);
    }
}
