package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class MFARequiredException extends ApplicationBaseException {
    public MFARequiredException(String message) {
        super(message);
    }

    public MFARequiredException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public MFARequiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public MFARequiredException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
