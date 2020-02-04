package bg.sofia.uni.fmi.mjt.authapp.session;

import java.util.UUID;

public class Session {
    private static final int SECONDS_TO_LIVE = 180;
    private final String sessionId;
    private final int secondsToLive;

    public static Session createSession() {
        return new Session();
    }

    private Session() {
        sessionId = UUID.randomUUID().toString();
        secondsToLive = SECONDS_TO_LIVE;
    }

    public String getId() {
        return sessionId;
    }

    public int getSecondsToLive() {
        return secondsToLive;
    }
}
