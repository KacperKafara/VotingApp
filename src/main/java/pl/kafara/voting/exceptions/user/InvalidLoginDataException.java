package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class InvalidLoginDataException extends ApplicationBaseException {
    public InvalidLoginDataException(String message) {
        super(message);
    }

    public InvalidLoginDataException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public InvalidLoginDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLoginDataException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
