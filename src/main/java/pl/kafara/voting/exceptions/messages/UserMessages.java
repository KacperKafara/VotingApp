package pl.kafara.voting.exceptions.messages;

public class UserMessages {
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_VERIFIED = "User not verified";
    public static final String USER_BLOCKED = "User blocked";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String USERNAME_OR_EMAIL_ALREADY_EXISTS = "Username or email already exists";
    public static final String PHONE_NUMBER_ALREADY_EXISTS = "Phone number already exists";
    public static final String ACCOUNT_VERIFICATION_TOKEN_USED = "Account verification token used";
    public static final String ACCOUNT_VERIFICATION_TOKEN_EXPIRED = "Account verification token expired";
    public static final String RESET_PASSWORD_TOKEN_USED = "Reset password token used";
    public static final String RESET_PASSWORD_TOKEN_EXPIRED = "Reset password token expired";
    public static final String AUTHENTICATION_BLOCKED = "Authentication blocked";
    public static final String USER_MUST_HAVE_AT_LEAST_ONE_ROLE = "User must have at least one role";

    private UserMessages() {
    }
}
