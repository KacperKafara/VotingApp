package pl.kafara.voting.exceptions;

public class SurveyException extends ApplicationBaseException {
    public SurveyException(String message) {
        super(message);
    }

    public SurveyException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public SurveyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SurveyException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
