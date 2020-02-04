package bg.sofia.uni.fmi.mjt.authapp.requesthandler;

import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.ADMIN_PROMOTED_MESSAGE_TEMPLATE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.ADMIN_REMOVED_MESSAGE_TEMPLATE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.ADMIN_ROLE_REQUIRED_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.ALREADY_LOGGED_IN_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.ALREADY_REGISTERED_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.AUDIT_LOG_FILE_PATH;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.DELETED_USER_MESSAGE_TEMPLATE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.FAILED_LOGIN_BLOCK_MESSAGE_TEMPLATE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.INVALID_SESSION_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.LOCK_CHANNEL_SECONDS;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.LOGGED_OUT_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.LOGIN_WITH_SESSION_SUCCESS_MESSAGE_TEMPLATE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.LOGIN_WITH_USERNAME_SUCCESS_MESSAGE_TEMPLATE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.MAX_LOGIN_ATTEMPTS;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.NOT_LOGGED_IN_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.ONLY_ONE_ADMIN_LEFT_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.PASSWORD_SALT_LENGTH;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.RESET_PASSWORD_MISMATCH_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.RESET_PASSWORD_SUCCESS_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.SUCCESSFUL_REGISTRATION_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.UPDATE_USER_SUCCESS_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.USERNAME_NOT_FOUND_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.USERNAME_TAKEN_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.USERS_FILE_PATH;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.WRONG_COMMAND_MESSAGE;
import static bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandlerConstants.WRONG_CREDENTIALS_MESSAGE;


import bg.sofia.uni.fmi.mjt.authapp.auditlog.LogType;
import bg.sofia.uni.fmi.mjt.authapp.auditlog.Logger;
import bg.sofia.uni.fmi.mjt.authapp.db.UsersFileHandler;
import bg.sofia.uni.fmi.mjt.authapp.exceptions.OpType;
import bg.sofia.uni.fmi.mjt.authapp.exceptions.SocketChannelException;
import bg.sofia.uni.fmi.mjt.authapp.security.PasswordHasher;
import bg.sofia.uni.fmi.mjt.authapp.session.Session;
import bg.sofia.uni.fmi.mjt.authapp.session.SessionStorage;
import bg.sofia.uni.fmi.mjt.authapp.socketio.SocketChannelIoHandler;
import bg.sofia.uni.fmi.mjt.authapp.users.Role;
import bg.sofia.uni.fmi.mjt.authapp.users.User;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class RequestHandler {

    private SessionStorage sessionStorage;
    private Logger logger;
    private UsersFileHandler usersFileHandler;
    private SocketChannelIoHandler channelIoHandler;
    private Set<SocketChannel> lockedChannels;
    private Map<SocketChannel, Integer> failedLoginAttempts;

    public RequestHandler() {
        sessionStorage = new SessionStorage();
        logger = new Logger(AUDIT_LOG_FILE_PATH);
        usersFileHandler = new UsersFileHandler(USERS_FILE_PATH);
        channelIoHandler = new SocketChannelIoHandler();
        lockedChannels = ConcurrentHashMap.newKeySet();
        failedLoginAttempts = new ConcurrentHashMap<>();
    }

    public void registerUser(SocketChannel currentChannel, String command) {
        String[] tokens = command.split("\\s+");
        if (sessionStorage.isUserLoggedIn(currentChannel)) {
            channelIoHandler.writeToSocketChannel(currentChannel,
                    ALREADY_REGISTERED_MESSAGE);
        } else {
            String passwordSalt = PasswordHasher.generateSalt(PASSWORD_SALT_LENGTH).get();
            String password = PasswordHasher.hashPassword(tokens[4], passwordSalt).get();
            User user = User.builder()
                    .username(tokens[2])
                    .password(password)
                    .firstName(tokens[6])
                    .lastName(tokens[8])
                    .email(tokens[10])
                    .role(Role.UNAUTHENTICATED)
                    .passwordSalt(passwordSalt)
                    .build();
            if (usersFileHandler.saveUserInFile(user)) {
                channelIoHandler.writeToSocketChannel(currentChannel,
                        SUCCESSFUL_REGISTRATION_MESSAGE);
                loginWithUsername(user.getUsername(), currentChannel, tokens[4]);
            } else {
                channelIoHandler.writeToSocketChannel(currentChannel,
                        USERNAME_TAKEN_MESSAGE);
            }
        }
    }

    public void loginUser(SocketChannel currentChannel, String command) {
        String[] tokens = command.split("\\s+");
        if (sessionStorage.isUserLoggedIn(currentChannel)) {
            channelIoHandler.writeToSocketChannel(currentChannel, ALREADY_LOGGED_IN_MESSAGE);
        } else if (tokens[1].equalsIgnoreCase("--username")) {
            loginWithUsername(tokens[2], currentChannel, tokens[4]);
        } else if (tokens[1].equalsIgnoreCase("--session-id")) {
            loginWithSessionId(tokens[2], currentChannel);
        }
    }

    private void loginWithUsername(String username, SocketChannel currentChannel, String password) {
        if (lockedChannels.contains(currentChannel)) {
            channelIoHandler.writeToSocketChannel(currentChannel,
                    MessageFormat.format(FAILED_LOGIN_BLOCK_MESSAGE_TEMPLATE,
                            LOCK_CHANNEL_SECONDS, MAX_LOGIN_ATTEMPTS));
            return;
        }
        User user = usersFileHandler.findUserInFile(username);
        if (user != null &&
                PasswordHasher.verifyPassword(
                        password, user.getPassword(), user.getPasswordSalt())) {
            Session session = sessionStorage.newSession(user, currentChannel);
            channelIoHandler.writeToSocketChannel(currentChannel,
                    MessageFormat.format(LOGIN_WITH_SESSION_SUCCESS_MESSAGE_TEMPLATE,
                            session.getId()));
        } else {
            failedLoginAttempts.merge(currentChannel, 1, Integer::sum);
            if (failedLoginAttempts.get(currentChannel) >= 3) {
                lockChannel(currentChannel);
                failedLoginAttempts.remove(currentChannel);
            }
            channelIoHandler.writeToSocketChannel(currentChannel, WRONG_CREDENTIALS_MESSAGE);
            logger.logFailedLogin(username, currentChannel.socket().getInetAddress());
        }
    }

    private void loginWithSessionId(String sessionId, SocketChannel currentChannel) {
        if (lockedChannels.contains(currentChannel)) {
            channelIoHandler.writeToSocketChannel(currentChannel,
                    MessageFormat.format(FAILED_LOGIN_BLOCK_MESSAGE_TEMPLATE,
                            LOCK_CHANNEL_SECONDS, MAX_LOGIN_ATTEMPTS));
            return;
        }
        Session session = sessionStorage.getById(sessionId);
        User loggedUser = sessionStorage.getUserBySession(session);
        if (session != null) {
            sessionStorage.login(session, currentChannel);
            channelIoHandler.writeToSocketChannel(currentChannel,
                    MessageFormat.format(LOGIN_WITH_USERNAME_SUCCESS_MESSAGE_TEMPLATE,
                            loggedUser.getUsername()));
        } else {
            channelIoHandler.writeToSocketChannel(currentChannel, INVALID_SESSION_MESSAGE);
            logger.logFailedLogin(null, currentChannel.socket().getInetAddress());
            failedLoginAttempts.merge(currentChannel, 1, Integer::sum);
            if (failedLoginAttempts.get(currentChannel) >= MAX_LOGIN_ATTEMPTS) {
                lockChannel(currentChannel);
                failedLoginAttempts.remove(currentChannel);
            }
        }
    }

    private void lockChannel(SocketChannel currentChannel) {
        lockedChannels.add(currentChannel);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                lockedChannels.remove(currentChannel);
            }
        }, LOCK_CHANNEL_SECONDS * 1000);
    }

    public void updateUser(SocketChannel currentChannel, String command) {
        String[] tokens = command.split("\\s+");
        if (sessionStorage.isUserLoggedIn(currentChannel) &&
                sessionStorage.hasValidSession(currentChannel) &&
                sessionStorage.getById(tokens[2]) != null) {
            User currentUser = sessionStorage.getUserByChannel(currentChannel);
            String oldUsername = currentUser.getUsername();

            for (int i = 3; i < tokens.length - 1; i += 2) {
                if (tokens[i].equalsIgnoreCase("--new-username")) {
                    currentUser.setUsername(tokens[i + 1]);
                } else if (tokens[i].equalsIgnoreCase("--new-first-name")) {
                    currentUser.setFirstName(tokens[i + 1]);
                } else if (tokens[i].equalsIgnoreCase("--new-last-name")) {
                    currentUser.setLastName(tokens[i + 1]);
                } else if (tokens[i].equalsIgnoreCase("--new-email")) {
                    currentUser.setEmail(tokens[i + 1]);
                }
            }
            usersFileHandler.updateUserInFile(oldUsername, currentUser);
            channelIoHandler.writeToSocketChannel(currentChannel, UPDATE_USER_SUCCESS_MESSAGE);
        }
    }

    public void resetPassword(SocketChannel currentChannel, String command) {
        String[] tokens = command.split("\\s+");
        if (sessionStorage.isUserLoggedIn(currentChannel) &&
                sessionStorage.hasValidSession(currentChannel) &&
                sessionStorage.getById(tokens[2]) != null) {
            User currentUser = sessionStorage.getUserByChannel(currentChannel);
            String oldPassword = tokens[6];
            String passwordSalt = currentUser.getPasswordSalt();
            if (!PasswordHasher.verifyPassword(oldPassword,
                    currentUser.getPassword(), passwordSalt)) {
                channelIoHandler.writeToSocketChannel(currentChannel, RESET_PASSWORD_MISMATCH_MESSAGE);
            } else {
                String newPassword = PasswordHasher.hashPassword(tokens[8], passwordSalt).get();
                currentUser.setPassword(newPassword);
                usersFileHandler.updateUserInFile(currentUser.getUsername(), currentUser);
                channelIoHandler.writeToSocketChannel(currentChannel, RESET_PASSWORD_SUCCESS_MESSAGE);
            }
        }
    }

    public void addAdminUser(SocketChannel currentChannel, String command) {
        String[] tokens = command.split("\\s+");
        String usernameOfNewAdmin = tokens[4];
        User currentUser = sessionStorage.getUserByChannel(currentChannel);
        // findUserInFile accepts username, so I first fetch the user by channel
        currentUser = usersFileHandler.findUserInFile(currentUser.getUsername());
        logger.logBeforeChangeStart(currentChannel, currentUser,
                LogType.ADD_ADMIN, usernameOfNewAdmin);
        boolean isSuccessful = false;
        User userToChange = usersFileHandler.findUserInFile(usernameOfNewAdmin);
        if (!currentUser.getRole().equals(Role.ADMIN)) {
            channelIoHandler.writeToSocketChannel(currentChannel,
                    ADMIN_ROLE_REQUIRED_MESSAGE);
        } else if (userToChange == null) {
            channelIoHandler.writeToSocketChannel(currentChannel,
                    USERNAME_NOT_FOUND_MESSAGE);
        } else {
            userToChange.setRole(Role.ADMIN);
            usersFileHandler.updateUserInFile(usernameOfNewAdmin, userToChange);
            channelIoHandler.writeToSocketChannel(currentChannel,
                    MessageFormat.format(ADMIN_PROMOTED_MESSAGE_TEMPLATE, usernameOfNewAdmin));
            isSuccessful = true;
        }
        logger.logAfterChangeFinish(currentChannel, currentUser, LogType.ADD_ADMIN,
                usernameOfNewAdmin, isSuccessful);
    }

    public void removeAdminUser(SocketChannel currentChannel, String command) {
        String[] tokens = command.split("\\s+");
        String usernameOfChangedUser = tokens[4];
        User currentUser = sessionStorage.getUserByChannel(currentChannel);
        currentUser = usersFileHandler.findUserInFile(currentUser.getUsername());
        User userToChange = usersFileHandler.findUserInFile(usernameOfChangedUser);
        logger.logBeforeChangeStart(currentChannel, currentUser,
                LogType.REMOVE_ADMIN, usernameOfChangedUser);
        boolean isSuccessful = false;
        if (!currentUser.getRole().equals(Role.ADMIN)) {
            channelIoHandler.writeToSocketChannel(currentChannel,
                    ADMIN_ROLE_REQUIRED_MESSAGE);
        } else if (userToChange == null) {
            channelIoHandler.writeToSocketChannel(currentChannel,
                    USERNAME_NOT_FOUND_MESSAGE);
        } else if (userToChange.equals(currentUser) && usersFileHandler.getAdminCount() < 2) {
            channelIoHandler.writeToSocketChannel(currentChannel,
                    ONLY_ONE_ADMIN_LEFT_MESSAGE);
        } else {
            userToChange.setRole(Role.UNAUTHENTICATED);
            usersFileHandler.updateUserInFile(usernameOfChangedUser, userToChange);
            channelIoHandler.writeToSocketChannel(currentChannel,
                    MessageFormat.format(ADMIN_REMOVED_MESSAGE_TEMPLATE, usernameOfChangedUser));
            isSuccessful = true;
        }
        logger.logAfterChangeFinish(currentChannel, currentUser, LogType.REMOVE_ADMIN,
                usernameOfChangedUser, isSuccessful);
    }

    public void deleteUser(SocketChannel currentChannel, String username) {
        User currentUser = sessionStorage.getUserByChannel(currentChannel);
        currentUser = usersFileHandler.findUserInFile(currentUser.getUsername());
        if (currentUser != null && !currentUser.getRole().equals(Role.ADMIN)) {
            channelIoHandler.writeToSocketChannel(currentChannel,
                    ADMIN_ROLE_REQUIRED_MESSAGE);
        } else {
            sessionStorage.logout(usersFileHandler.findUserInFile(username));
            usersFileHandler.updateUserInFile(username, null);
            channelIoHandler.writeToSocketChannel(currentChannel,
                    MessageFormat.format(DELETED_USER_MESSAGE_TEMPLATE, username));
        }
    }

    public void logoutUser(SocketChannel currentChannel, String command) {
        String[] tokens = command.split("\\s+");
        if (!sessionStorage.isUserLoggedIn(currentChannel)) {
            channelIoHandler.writeToSocketChannel(currentChannel, NOT_LOGGED_IN_MESSAGE);
        } else if (!sessionStorage.hasValidSession(currentChannel) ||
                sessionStorage.getById(tokens[2]) == null) {
            channelIoHandler.writeToSocketChannel(currentChannel, INVALID_SESSION_MESSAGE);
        } else {
            sessionStorage.logout(sessionStorage.getUserByChannel(currentChannel));
            channelIoHandler.writeToSocketChannel(currentChannel, LOGGED_OUT_MESSAGE);
        }
    }

    public void disconnectFromServer(SocketChannel currentChannel) {
        sessionStorage.disconnect(currentChannel);
        try {
            currentChannel.close();
        } catch (IOException e) {
            throw new SocketChannelException(OpType.CLOSE);
        }
    }

    public void handleWrongCommand(SocketChannel currentChannel) {
        channelIoHandler.writeToSocketChannel(currentChannel, WRONG_COMMAND_MESSAGE);
    }

    public String readFromSocketChannel(SocketChannel currentChannel) {
        return channelIoHandler.readFromSocketChannel(currentChannel);
    }
}
