package bg.sofia.uni.fmi.mjt.authapp.session;

import bg.sofia.uni.fmi.mjt.authapp.users.User;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStorage {

    private static final String SESSION_TIMEOUT_MESSAGE =
            "Your session has timed out. You've been logged out.";
    private Map<Session, User> userBySession;
    private Map<User, Session> sessionByUser;
    private Map<String, Session> sessionsById;
    private Map<SocketChannel, User> userByChannel;
    private Map<User, SocketChannel> channelByUser;

    public SessionStorage() {
        userBySession = new ConcurrentHashMap<>();
        sessionByUser = new ConcurrentHashMap<>();
        sessionsById = new ConcurrentHashMap<>();
        userByChannel = new ConcurrentHashMap<>();
        channelByUser = new ConcurrentHashMap<>();
    }

    public Session newSession(User user, SocketChannel channel) {
        Session session = Session.createSession();
        addUserSession(user, session);
        login(session, channel);
        Timer killSessionTimer = new Timer();
        killSessionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                logout(user);
                try {
                    if (channel.isOpen()) {
                        channel.write(ByteBuffer.wrap(
                                SESSION_TIMEOUT_MESSAGE.getBytes()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, session.getSecondsToLive() * 1000);
        return session;
    }

    private void addUserSession(User user, Session session) {
        userBySession.put(session, user);
        sessionByUser.put(user, session);
        sessionsById.put(session.getId(), session);
    }

    public void login(Session session, SocketChannel channel) {
        User user = userBySession.get(session);
        userByChannel.put(channel, user);
        channelByUser.put(user, channel);
    }

    public void disconnect(SocketChannel channel) {
        if (channel != null) {
            User user = userByChannel.get(channel);
            if (user != null) {
                userByChannel.remove(channel);
                channelByUser.remove(user);
            }
        }
    }

    public void logout(User user) {
        if (user != null) {
            SocketChannel channel = channelByUser.get(user);
            disconnect(channel);
            Session session = sessionByUser.get(user);
            if (session != null) {
                userBySession.remove(session);
                sessionByUser.remove(user);
                sessionsById.remove(session.getId());
            }
        }
    }

    public boolean isUserLoggedIn(SocketChannel channel) {
        return userByChannel.containsKey(channel);
    }

    public Session getById(String sessionId) {
        return sessionId != null ? sessionsById.get(sessionId) : null;
    }

    public User getUserBySession(Session session) {
        return session != null ? userBySession.get(session) : null;
    }

    public boolean hasValidSession(SocketChannel channel) {
        if (channel == null) {
            return false;
        }
        User channelUser = userByChannel.get(channel);
        return sessionByUser.containsKey(channelUser);
    }

    public User getUserByChannel(SocketChannel channel) {
        return channel != null ? userByChannel.get(channel) : null;
    }
}
