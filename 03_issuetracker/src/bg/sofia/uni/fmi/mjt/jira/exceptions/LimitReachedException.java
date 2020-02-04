package bg.sofia.uni.fmi.mjt.jira.exceptions;

public class LimitReachedException extends RuntimeException {
    public LimitReachedException(String message) {
        super(message);
    }
}
