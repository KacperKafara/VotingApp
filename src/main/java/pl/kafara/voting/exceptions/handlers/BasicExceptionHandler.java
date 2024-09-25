package pl.kafara.voting.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import pl.kafara.voting.exceptions.ApplicationBaseException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.messages.UserMessages;

import java.security.NoSuchAlgorithmException;

@ControllerAdvice
@Slf4j
public class BasicExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        if(e.getCause() instanceof ConstraintViolationException ex) {
            String constraintName = ex.getConstraintName();
            if(constraintName.equals("users_username_key") || constraintName.equals("personal_data_email_key")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ExceptionResponse(UserMessages.USERNAME_OR_EMAIL_ALREADY_EXISTS, ExceptionCodes.USERNAME_OR_EMAIL_ALREADY_EXISTS));
            } else if (constraintName.equals("personal_data_phone_number_key")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ExceptionResponse(UserMessages.PHONE_NUMBER_ALREADY_EXISTS, ExceptionCodes.PHONE_NUMBER_ALREADY_EXISTS));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ExceptionResponse(GenericMessages.CONSTRAINT_VIOLATION, ExceptionCodes.CONSTRAINT_VIOLATION));
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionResponse(GenericMessages.CONSTRAINT_VIOLATION, ExceptionCodes.CONSTRAINT_VIOLATION));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error("Exception: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(GenericMessages.INTERNAL_SERVER_ERROR, ExceptionCodes.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        sb.append(GenericMessages.VALIDATION_ERROR + " ");
        for (FieldError error : e.getFieldErrors()) {
            sb.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(", ");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(sb.toString(), ExceptionCodes.VALIDATION_ERROR));
    }

    @ExceptionHandler(GenericJDBCException.class)
    ResponseEntity<ExceptionResponse> handleJDBCException(GenericJDBCException e) {
        log.error("JDBC Exception: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(GenericMessages.JDBC_ERROR, ExceptionCodes.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ExceptionResponse> handleResponseStatusException(ResponseStatusException e) {
        if(e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            log.error("Internal Server Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ExceptionResponse(GenericMessages.INTERNAL_SERVER_ERROR, ExceptionCodes.INTERNAL_SERVER_ERROR));
        }

        if(e.getCause() instanceof ApplicationBaseException ex) {
            if(ex.getExceptionCode() != null) {
                return ResponseEntity.status(e.getStatusCode())
                        .body(new ExceptionResponse(e.getReason(), ex.getExceptionCode()));
            }
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ExceptionResponse(e.getReason(), ExceptionCodes.SOMETHING_WENT_WRONG));
        }
        return ResponseEntity.status(e.getStatusCode())
                .body(new ExceptionResponse(e.getReason(), ExceptionCodes.SOMETHING_WENT_WRONG));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<ExceptionResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ExceptionResponse(GenericMessages.MEDIA_TYPE_NOT_SUPPORTED, ExceptionCodes.MEDIA_TYPE_NOT_SUPPORTED));
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(e.getMessage(), e.getExceptionCode()));
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    ResponseEntity<ExceptionResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException e) {
        log.error("NoSuchAlgorithmException: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(GenericMessages.SOMETHING_WENT_WRONG, ExceptionCodes.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(GenericMessages.RESOURCE_NOT_FOUND, ExceptionCodes.RESOURCE_NOT_FOUND));
    }
}
