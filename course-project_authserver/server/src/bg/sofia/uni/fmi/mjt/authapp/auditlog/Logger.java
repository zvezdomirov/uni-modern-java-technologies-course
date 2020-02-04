package bg.sofia.uni.fmi.mjt.authapp.auditlog;

import bg.sofia.uni.fmi.mjt.authapp.exceptions.FileIoException;
import bg.sofia.uni.fmi.mjt.authapp.users.User;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;

public class Logger {

    private static final String LOG_MESSAGE_TEMPLATE_BASE =
            "Date: {0}; Type: {1}; User IP Address: {2}; User: {3}";
    private static final String LOG_BEFORE_CHANGING_USER_MESSAGE_TEMPLATE =
            LOG_MESSAGE_TEMPLATE_BASE + "; Affected User: {4}\r\n";
    private static final String LOG_AFTER_CHANGING_USER_MESSAGE_TEMPLATE =
            LOG_BEFORE_CHANGING_USER_MESSAGE_TEMPLATE + " Is successful: {5}\r\n";
    private Path logFile;

    public Logger(String logFilePath) {
        logFile = Path.of(logFilePath);
    }

    public void logBeforeChangeStart(SocketChannel currentChannel, User currentUser,
                                     LogType logType, String usernameOfChangedUser) {
        String logMessage = MessageFormat.format(
                LOG_BEFORE_CHANGING_USER_MESSAGE_TEMPLATE,
                LocalDateTime.now(), logType.getName(),
                currentChannel.socket().getInetAddress().toString(),
                currentUser.getUsername(), usernameOfChangedUser);
        logMessage(logMessage);
    }

    public void logAfterChangeFinish(SocketChannel currentChannel, User currentUser,
                                     LogType logType, String usernameOfChangedUser,
                                     boolean isSuccessful) {
        String logMessage = MessageFormat.format(LOG_AFTER_CHANGING_USER_MESSAGE_TEMPLATE,
                LocalDateTime.now(), logType.getName(),
                currentChannel.socket().getInetAddress(), currentUser.getUsername(),
                usernameOfChangedUser, isSuccessful);
        logMessage(logMessage);
    }

    public void logFailedLogin(String username, InetAddress ipAddress) {
        String logMessage = MessageFormat.format(
                LOG_MESSAGE_TEMPLATE_BASE + "\r\n",
                LocalDateTime.now(), LogType.FAILED_LOGIN.getName(),
                ipAddress.toString(), username);
        logMessage(logMessage);
    }

    public void logMessage(String message) {
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardOpenOption.APPEND)) {
            writer.write(message);
        } catch (IOException e) {
            throw new FileIoException(logFile.getFileName().toString());
        }
    }
}
