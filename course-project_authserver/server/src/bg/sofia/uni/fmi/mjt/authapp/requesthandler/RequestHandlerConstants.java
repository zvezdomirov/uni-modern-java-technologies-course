package bg.sofia.uni.fmi.mjt.authapp.requesthandler;

class RequestHandlerConstants {
    static final int LOCK_CHANNEL_SECONDS = 10;
    static final int MAX_LOGIN_ATTEMPTS = 3;
    static final String USERS_FILE_PATH = "users.txt";
    static final String AUDIT_LOG_FILE_PATH = "audit-log.txt";
    static final String ALREADY_REGISTERED_MESSAGE =
            "You are already registered and logged in";
    static final String SUCCESSFUL_REGISTRATION_MESSAGE =
            "Registration successful. Logging you in...";
    static final String USERNAME_TAKEN_MESSAGE =
            "There is already a user with this username";
    static final String ALREADY_LOGGED_IN_MESSAGE =
            "You are already logged in";
    static final String NOT_LOGGED_IN_MESSAGE =
            "You are not logged in";
    static final String UPDATE_USER_SUCCESS_MESSAGE =
            "Successfully updated user information";
    static final String RESET_PASSWORD_SUCCESS_MESSAGE =
            "You've successfully reset your password";
    static final String RESET_PASSWORD_MISMATCH_MESSAGE =
            "Old password doesn't match";
    static final String ADMIN_ROLE_REQUIRED_MESSAGE =
            "You should be an Administrator to perform this action";
    static final String USERNAME_NOT_FOUND_MESSAGE =
            "No user with this username is found";
    static final String ADMIN_PROMOTED_MESSAGE_TEMPLATE =
            "Successfully changed the role of User \"{0}\" to Administrator";
    static final String ONLY_ONE_ADMIN_LEFT_MESSAGE =
            "You are currently the only Administrator, " +
                    "therefore you can't change that role at the moment.";
    static final String ADMIN_REMOVED_MESSAGE_TEMPLATE =
            "Successfully removed Administrator privileges from \"{0}\"";
    static final String DELETED_USER_MESSAGE_TEMPLATE =
            "Successfully deleted user \"{0}\"";
    static final String LOGGED_OUT_MESSAGE =
            "Successfully logged out";
    static final String FAILED_LOGIN_BLOCK_MESSAGE_TEMPLATE =
            "You''ve been blocked for {0} seconds due to {1} failed login attempts.";
    static final String LOGIN_WITH_SESSION_SUCCESS_MESSAGE_TEMPLATE =
            "Successfully logged in. Here is your session id: {0}";
    static final String INVALID_SESSION_MESSAGE =
            "Your session is not valid";
    static final String LOGIN_WITH_USERNAME_SUCCESS_MESSAGE_TEMPLATE =
            "Successfully logged in. Hello, {0}";
    static final String WRONG_CREDENTIALS_MESSAGE =
            "Wrong credentials entered, please try again";
    static final String WRONG_COMMAND_MESSAGE =
            "Wrong command format";
    static final int PASSWORD_SALT_LENGTH = 512;

    // Make the class non-instantiatable
    private RequestHandlerConstants() {
    }
}
