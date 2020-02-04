package bg.sofia.uni.fmi.mjt.chat;

public class MessageConstants {
    static final String ALREADY_LOGGED_IN_MESSAGE =
            "You are already registered and logged in";
    static final String USERNAME_ALREADY_TAKEN_MESSAGE_TEMPLATE =
            "Username [{0}] is already taken";
    static final String SUCCESSFUL_REGISTRATION_MESSAGE_TEMPLATE =
            "Registration successful! Hello, {0}";
    static final String NOT_LOGGED_IN_MESSAGE =
            "Error: You are not logged in";
    static final String USER_NOT_FOUND_MESSAGE_TEMPLATE =
            "User [{0}] is not currently connected";
    static final String CHAT_MESSAGE_TEMPLATE =
            "{0} # {1}";
    static final String SELECTOR_OPEN_EXCEPTION_MESSAGE =
            "An error occurred while trying to open the selector";
    static final String SERVER_ERROR_MESSAGE =
            "Something went wrong with the server";
    static final String NO_READY_CHANNELS_MESSAGE =
            "No channels are ready at the moment";
    static final String SERVER_STARTED_MESSAGE =
            "Server started listening on port 8080";
    static final String INVALID_COMMAND_MESSAGE = "Invalid command";
    // Make the class non-instantiable
    private MessageConstants() {
    }
}
