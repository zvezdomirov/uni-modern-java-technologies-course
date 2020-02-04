package bg.sofia.uni.fmi.mjt.authapp.exceptions;

import java.text.MessageFormat;

public class SocketChannelException extends RuntimeException {

    public static final String EXCEPTION_MESSAGE_TEMPLATE =
            "An error occurred while trying to perform {0} on socket channel";

    public SocketChannelException(OpType opType) {
        super(MessageFormat.format(EXCEPTION_MESSAGE_TEMPLATE, opType.name()));
    }
}
