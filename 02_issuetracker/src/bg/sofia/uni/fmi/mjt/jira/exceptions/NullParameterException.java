package bg.sofia.uni.fmi.mjt.jira.exceptions;

public class NullParameterException extends RuntimeException{
    public NullParameterException() {
    }
    public NullParameterException(String parameterName) {
        super(String.format(
                "Parameter %s cannot be null",
                parameterName));
    }
}
