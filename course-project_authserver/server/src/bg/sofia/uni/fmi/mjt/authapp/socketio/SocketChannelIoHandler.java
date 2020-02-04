package bg.sofia.uni.fmi.mjt.authapp.socketio;

import bg.sofia.uni.fmi.mjt.authapp.exceptions.OpType;
import bg.sofia.uni.fmi.mjt.authapp.exceptions.SocketChannelException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelIoHandler {

    private static final int BUFFER_CAPACITY = 1024;
    private ByteBuffer buffer;

    public SocketChannelIoHandler() {
        buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
    }

    public void writeToSocketChannel(SocketChannel channel, String message) {
        buffer.clear();
        buffer.put(message
                .getBytes());
        buffer.flip();
        try {
            channel.write(buffer);
        } catch (IOException e) {
            throw new SocketChannelException(OpType.WRITE);
        }
    }

    public String readFromSocketChannel(SocketChannel currentChannel) {
        buffer.clear();
        try {
            currentChannel.read(buffer);
        } catch (IOException e) {
            throw new SocketChannelException(OpType.READ);
        }
        buffer.flip();
        return new String(buffer.array(), 0, buffer.limit());
    }
}
