package pt.ua.nicevolunteers.volunteer.domain.valueobject;

import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import pt.ua.nicevolunteers.volunteer.domain.exception.InvalidEmailException;

@Embeddable
public class Email {

    @Column(name = "email_value", nullable = false, unique = true)
    private String value;

    protected Email() {
        // JPA
    }

    public Email(String value) {
        if (!isValidInstitutionalEmail(value)) {
            throw new InvalidEmailException("Email must be institutional (ex: @ua.pt)");
        }
        this.value = value.toLowerCase();
    }

    private boolean isValidInstitutionalEmail(String email) {
        if (email == null) return false;

        String regex = "^[A-Za-z0-9._%+-]+@ua\\.pt$";
        return Pattern.matches(regex, email);
    }

    public String getValue() {
        return value;
    }
}
