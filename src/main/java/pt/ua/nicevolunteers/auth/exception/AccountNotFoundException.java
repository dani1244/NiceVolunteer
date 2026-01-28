package pt.ua.nicevolunteers.auth.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super("Conta não encontrada.");
    }
}
