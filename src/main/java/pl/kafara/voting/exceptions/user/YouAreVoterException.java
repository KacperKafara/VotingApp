package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class YouAreVoterException extends ApplicationBaseException {

    public YouAreVoterException(String message) {
        super(message);
    }

    public YouAreVoterException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public YouAreVoterException(String message, Throwable cause) {
        super(message, cause);
    }

    public YouAreVoterException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
