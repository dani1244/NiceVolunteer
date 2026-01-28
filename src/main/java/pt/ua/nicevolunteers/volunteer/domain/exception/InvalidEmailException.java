package pt.ua.nicevolunteers.volunteer.domain.exception;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException() {
        super();
    }

    public InvalidEmailException(String message) {
        super(message);
    }
}
