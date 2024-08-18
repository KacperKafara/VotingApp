package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class AccountNotActiveException extends ApplicationBaseException {
    public AccountNotActiveException(String message) {
        super(message);
    }

    public AccountNotActiveException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public AccountNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotActiveException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
