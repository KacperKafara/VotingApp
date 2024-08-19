package pl.kafara.voting.exceptions;

public class CreationException extends ApplicationBaseException {
    public CreationException(String message) {
        super(message);
    }

    public CreationException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public CreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreationException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
