package bg.sofia.uni.fmi.mjt.jira.exceptions;

public class UnresolvableIssueException extends RuntimeException {
    public UnresolvableIssueException(String message) {
        super(message);
    }
}
