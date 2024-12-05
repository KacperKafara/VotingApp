package pl.kafara.voting.exceptions;

public class VotingOrSurveyNotActiveException extends ApplicationBaseException {
    public VotingOrSurveyNotActiveException(String message) {
        super(message);
    }

    public VotingOrSurveyNotActiveException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public VotingOrSurveyNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public VotingOrSurveyNotActiveException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
