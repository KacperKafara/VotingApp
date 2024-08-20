package pl.kafara.voting.exceptions.handlers;

public record ExceptionResponse (
        String message,
        String exceptionCode
) {
}
