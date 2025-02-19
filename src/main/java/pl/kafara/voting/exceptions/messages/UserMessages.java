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
    public static final String CANNOT_BLOCK_YOURSELF = "Cannot block yourself";
    public static final String CANNOT_UNBLOCK_YOURSELF = "Cannot unblock yourself";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String CANNOT_MODIFY_YOURSELF_ROLES = "Cannot modify yourself roles";
    public static final String OPTIMISTIC_LOCK = "You are not working at the latest version of the data.";
    public static final String ROLE_REQUEST_NOT_FOUND = "Role request not found";
    public static final String ACCEPT_OWN_REQUEST_ROLE = "You cannot accept your own request";
    public static final String REJECT_OWN_REQUEST_ROLE = "You cannot reject your own request";
    public static final String USER_ALREADY_HAS_VOTER_ROLE = "User already has voter role";
    public static final String REFRESH_TOKEN_VERIFICATION_EXCEPTION = "Refresh token verification exception";
    public static final String FIRST_ACTIVATE_2FA = "First activate 2FA";
    public static final String TOTP_AUTHORISATION_ALREADY_ACTIVE = "2FA already active";
    public static final String MFA_REQUIRED = "MFA required";
    public static final String OAUTH_TOKEN_VERIFICATION_FAILED = "OAuth token verification failed";
    public static final String ROLE_REQUEST_ALREADY_RESOLVED = "Role request already resolved";
    public static final String PARLIAMENTARY_CLUB_NOT_FOUND = "Parliamentary club not found";
    public static final String CANT_RESET_PASSWORD = "Can't reset password for google account";

    private UserMessages() {
    }
}
