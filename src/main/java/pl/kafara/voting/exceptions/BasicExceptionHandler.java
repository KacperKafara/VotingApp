package pl.kafara.voting.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BasicExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public void handleConstraintViolationException(ConstraintViolationException e) {
        System.out.println("ConstraintViolationException: " + e.getMessage());
    }
}
