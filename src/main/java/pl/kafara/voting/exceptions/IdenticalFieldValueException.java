package pl.kafara.voting.exceptions;

public class IdenticalFieldValueException extends ApplicationBaseException {
    public IdenticalFieldValueException(String message) {
        super(message);
    }

    public IdenticalFieldValueException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public IdenticalFieldValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdenticalFieldValueException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
