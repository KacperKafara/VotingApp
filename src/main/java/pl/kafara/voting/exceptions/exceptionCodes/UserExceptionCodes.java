package pl.kafara.voting.exceptions.exceptionCodes;

public class UserExceptionCodes {
    public static final String USER_BLOCKED = "USER_BLOCKED";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String SOMETHING_WENT_WRONG = "SOMETHING_WENT_WRONG";
    public static final String MEDIA_TYPE_NOT_SUPPORTED = "MEDIA_TYPE_NOT_SUPPORTED";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";
    public static final String USERNAME_OR_EMAIL_ALREADY_EXISTS = "USERNAME_OR_EMAIL_ALREADY_EXISTS";
    public static final String PHONE_NUMBER_ALREADY_EXISTS = "PHONE_NUMBER_ALREADY_EXISTS";
    public static final String CONSTRAINT_VIOLATION = "CONSTRAINT_VIOLATION";
    public static final String ACCOUNT_VERIFICATION_TOKEN_USED = "ACCOUNT_VERIFICATION_TOKEN_USED";
    public static final String ACCOUNT_VERIFICATION_TOKEN_EXPIRED = "ACCOUNT_VERIFICATION_TOKEN_EXPIRED";
    public static final String USER_NOT_VERIFIED = "USER_NOT_VERIFIED";
    public static final String RESET_PASSWORD_TOKEN_USED = "RESET_PASSWORD_TOKEN_USED";
    public static final String RESET_PASSWORD_TOKEN_EXPIRED = "RESET_PASSWORD_TOKEN_EXPIRED";
    public static final String USER_WITH_EMAIL_NOT_FOUND = "USER_WITH_EMAIL_NOT_FOUND";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String AUTHENTICATION_BLOCKED = "AUTHENTICATION_BLOCKED";
    public static final String USER_MUST_HAVE_AT_LEAST_ONE_ROLE = "USER_MUST_HAVE_AT_LEAST_ONE_ROLE";
    public static final String CANNOT_BLOCK_YOURSELF = "CANNOT_BLOCK_YOURSELF";
    public static final String CANNOT_UNBLOCK_YOURSELF = "CANNOT_UNBLOCK_YOURSELF";
    public static final String WRONG_PASSWORD = "WRONG_PASSWORD_CHANGE_PASSWORD";
    public static final String NOT_AUTHORIZED_TO_PERFORM_THIS_ACTION = "NOT_AUTHORIZED_TO_PERFORM_THIS_ACTION";
    public static final String CANNOT_MODIFY_YOURSELF_ROLES = "CANNOT_MODIFY_YOURSELF_ROLES";
    public static final String USER_OPTIMISTIC_LOCK = "USER_OPTIMISTIC_LOCK";
    public static final String MISSING_REQUEST_HEADER = "MISSING_REQUEST_HEADER";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String INVALID_ARGUMENT_TYPE = "INVALID_ARGUMENT_TYPE";

    private UserExceptionCodes() {
    }
}
