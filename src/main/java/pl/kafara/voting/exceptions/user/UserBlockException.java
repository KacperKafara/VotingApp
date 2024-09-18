package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class UserBlockException extends ApplicationBaseException {
    public UserBlockException(String message) {
        super(message);
    }

    public UserBlockException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public UserBlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserBlockException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
