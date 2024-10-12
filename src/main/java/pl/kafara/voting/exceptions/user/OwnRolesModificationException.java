package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class OwnRolesModificationException extends ApplicationBaseException {
    public OwnRolesModificationException(String message) {
        super(message);
    }

    public OwnRolesModificationException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public OwnRolesModificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OwnRolesModificationException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
