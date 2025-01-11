package pl.kafara.voting.exceptions;

public class SendEmailException extends RuntimeException {
    public SendEmailException(Throwable cause) {
        super(cause);
    }
}
