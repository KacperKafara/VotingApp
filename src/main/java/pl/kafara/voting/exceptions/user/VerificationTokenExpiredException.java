package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class VerificationTokenExpiredException extends ApplicationBaseException {
    public VerificationTokenExpiredException(String message) {
        super(message);
    }

    public VerificationTokenExpiredException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public VerificationTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationTokenExpiredException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
