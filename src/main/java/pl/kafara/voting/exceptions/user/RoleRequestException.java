package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class RoleRequestException extends ApplicationBaseException {
    public RoleRequestException(String message) {
        super(message);
    }

    public RoleRequestException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public RoleRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleRequestException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
