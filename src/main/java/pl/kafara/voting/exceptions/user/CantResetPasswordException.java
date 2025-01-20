package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class CantResetPasswordException extends ApplicationBaseException {
    public CantResetPasswordException(String message) {
        super(message);
    }

    public CantResetPasswordException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public CantResetPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public CantResetPasswordException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
