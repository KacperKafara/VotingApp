package pl.kafara.voting.exceptions;

public class ApplicationOptimisticLockException extends ApplicationBaseException {
    public ApplicationOptimisticLockException(String message) {
        super(message);
    }

    public ApplicationOptimisticLockException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public ApplicationOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationOptimisticLockException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
