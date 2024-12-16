package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class OAuthVerificationException extends ApplicationBaseException {
    public OAuthVerificationException(String message) {
        super(message);
    }

    public OAuthVerificationException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public OAuthVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuthVerificationException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
