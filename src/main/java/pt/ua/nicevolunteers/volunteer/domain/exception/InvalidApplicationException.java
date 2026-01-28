package pt.ua.nicevolunteers.volunteer.domain.exception;

public class InvalidApplicationException extends RuntimeException {

    public InvalidApplicationException(String message) {
        super(message);
    }
}
