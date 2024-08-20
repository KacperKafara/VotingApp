package pl.kafara.voting.exceptions;

import lombok.Getter;

@Getter
public abstract class ApplicationBaseException extends Exception {
    protected String exceptionCode;

    public ApplicationBaseException(String message) {
        super(message);
    }

    public ApplicationBaseException(String message, String exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public ApplicationBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationBaseException(String message, String exceptionCode, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }
}
