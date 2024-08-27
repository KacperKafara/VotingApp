package pl.kafara.voting.exceptions.handlers;

public class ExceptionCodes {
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

    private ExceptionCodes() {
    }
}
