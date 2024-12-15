package pl.kafara.voting.exceptions.handlers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import pl.kafara.voting.exceptions.ApplicationBaseException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.OAuthGenericException;

import java.security.NoSuchAlgorithmException;

@ControllerAdvice
@Slf4j
public class BasicExceptionHandler {

    @ExceptionHandler(OAuthGenericException.class)
    public ResponseEntity<ExceptionResponse> handleOAuthGenericException(OAuthGenericException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(e.getMessage(), UserExceptionCodes.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        if(e.getCause() instanceof ConstraintViolationException ex) {
            String constraintName = ex.getConstraintName();
            if(constraintName.equals("users_username_key") || constraintName.equals("personal_data_email_key")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ExceptionResponse(UserMessages.USERNAME_OR_EMAIL_ALREADY_EXISTS, UserExceptionCodes.USERNAME_OR_EMAIL_ALREADY_EXISTS));
            } else if (constraintName.equals("personal_data_phone_number_key")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ExceptionResponse(UserMessages.PHONE_NUMBER_ALREADY_EXISTS, UserExceptionCodes.PHONE_NUMBER_ALREADY_EXISTS));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ExceptionResponse(GenericMessages.CONSTRAINT_VIOLATION, UserExceptionCodes.CONSTRAINT_VIOLATION));
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionResponse(GenericMessages.CONSTRAINT_VIOLATION, UserExceptionCodes.CONSTRAINT_VIOLATION));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(GenericMessages.INVALID_ARGUMENT_TYPE, UserExceptionCodes.INVALID_ARGUMENT_TYPE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error("Exception: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(GenericMessages.INTERNAL_SERVER_ERROR, UserExceptionCodes.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    ResponseEntity<ExceptionResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage(), UserExceptionCodes.MISSING_REQUEST_HEADER));
    }

    @ExceptionHandler(JWTDecodeException.class)
    ResponseEntity<ExceptionResponse> handleJWTDecodeException(JWTDecodeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(GenericMessages.INVALID_TOKEN, UserExceptionCodes.INVALID_TOKEN));
    }

    @ExceptionHandler(SignatureVerificationException.class)
    ResponseEntity<ExceptionResponse> handleSignatureVerificationException(SignatureVerificationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(GenericMessages.INVALID_TOKEN, UserExceptionCodes.INVALID_TOKEN));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        sb.append(GenericMessages.VALIDATION_ERROR + " ");
        for (FieldError error : e.getFieldErrors()) {
            sb.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(", ");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(sb.toString(), UserExceptionCodes.VALIDATION_ERROR));
    }

    @ExceptionHandler(GenericJDBCException.class)
    ResponseEntity<ExceptionResponse> handleJDBCException(GenericJDBCException e) {
        log.error("JDBC Exception: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(GenericMessages.JDBC_ERROR, UserExceptionCodes.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ExceptionResponse> handleResponseStatusException(ResponseStatusException e) {
        if(e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            log.error("Internal Server Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ExceptionResponse(GenericMessages.INTERNAL_SERVER_ERROR, UserExceptionCodes.INTERNAL_SERVER_ERROR));
        }

        if(e.getCause() instanceof ApplicationBaseException ex) {
            if(ex.getExceptionCode() != null) {
                return ResponseEntity.status(e.getStatusCode())
                        .body(new ExceptionResponse(e.getReason(), ex.getExceptionCode()));
            }
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ExceptionResponse(e.getReason(), UserExceptionCodes.SOMETHING_WENT_WRONG));
        }
        return ResponseEntity.status(e.getStatusCode())
                .body(new ExceptionResponse(e.getReason(), UserExceptionCodes.SOMETHING_WENT_WRONG));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<ExceptionResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ExceptionResponse(GenericMessages.MEDIA_TYPE_NOT_SUPPORTED, UserExceptionCodes.MEDIA_TYPE_NOT_SUPPORTED));
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
                .body(new ExceptionResponse(GenericMessages.SOMETHING_WENT_WRONG, UserExceptionCodes.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(GenericMessages.RESOURCE_NOT_FOUND, UserExceptionCodes.RESOURCE_NOT_FOUND));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ExceptionResponse> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(GenericMessages.NOT_AUTHORIZED_TO_PERFORM_THIS_ACTION, UserExceptionCodes.NOT_AUTHORIZED_TO_PERFORM_THIS_ACTION));
    }
}
