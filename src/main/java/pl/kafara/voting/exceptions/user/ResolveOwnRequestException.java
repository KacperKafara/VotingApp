package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class ResolveOwnRequestException extends ApplicationBaseException {
    public ResolveOwnRequestException(String message) {
        super(message);
    }

    public ResolveOwnRequestException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public ResolveOwnRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResolveOwnRequestException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
