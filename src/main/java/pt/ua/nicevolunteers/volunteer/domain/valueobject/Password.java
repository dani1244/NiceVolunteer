package pt.ua.nicevolunteers.volunteer.domain.valueobject;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import pt.ua.nicevolunteers.volunteer.domain.exception.WeakPasswordException;

@Embeddable
public class Password {

    private String hash;

    protected Password() { }

    public Password(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8) {
            throw new WeakPasswordException("Password must have at least 8 characters");
        }
        this.hash = hash(rawPassword);
    }

    private String hash(String raw) {
        return Integer.toHexString(raw.hashCode()); // simples para testes
    }

    public String getHashedValue() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Password)) {
            return false;
        }
        Password password = (Password) o;
        return Objects.equals(hash, password.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }
}
