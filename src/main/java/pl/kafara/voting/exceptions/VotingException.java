package pl.kafara.voting.exceptions;

public class VotingException extends ApplicationBaseException{
    public VotingException(String message) {
        super(message);
    }

    public VotingException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public VotingException(String message, Throwable cause) {
        super(message, cause);
    }

    public VotingException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
