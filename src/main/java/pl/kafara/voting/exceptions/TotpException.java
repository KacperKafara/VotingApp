package pl.kafara.voting.exceptions;

public class TotpException extends ApplicationBaseException {
    public TotpException(String message) {
        super(message);
    }

    public TotpException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public TotpException(String message, Throwable cause) {
        super(message, cause);
    }

    public TotpException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
