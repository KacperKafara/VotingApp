package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class TotpAuthorisationException extends ApplicationBaseException {
    public TotpAuthorisationException(String message) {
        super(message);
    }

    public TotpAuthorisationException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public TotpAuthorisationException(String message, Throwable cause) {
        super(message, cause);
    }


    public TotpAuthorisationException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
