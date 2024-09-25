package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class WrongPasswordException extends ApplicationBaseException {

    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongPasswordException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
