package pl.kafara.voting.exceptions;

public class NotFoundException extends ApplicationBaseException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
