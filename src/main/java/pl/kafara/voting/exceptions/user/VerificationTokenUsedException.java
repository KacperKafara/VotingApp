package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class VerificationTokenUsedException extends ApplicationBaseException {
    public VerificationTokenUsedException(String message) {
        super(message);
    }

    public VerificationTokenUsedException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public VerificationTokenUsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationTokenUsedException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
