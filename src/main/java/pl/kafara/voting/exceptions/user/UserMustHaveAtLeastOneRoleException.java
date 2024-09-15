package pl.kafara.voting.exceptions.user;

import pl.kafara.voting.exceptions.ApplicationBaseException;

public class UserMustHaveAtLeastOneRoleException extends ApplicationBaseException {

    public UserMustHaveAtLeastOneRoleException(String message) {
        super(message);
    }

    public UserMustHaveAtLeastOneRoleException(String message, String exceptionCode) {
        super(message, exceptionCode);
    }

    public UserMustHaveAtLeastOneRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserMustHaveAtLeastOneRoleException(String message, String exceptionCode, Throwable cause) {
        super(message, exceptionCode, cause);
    }
}
