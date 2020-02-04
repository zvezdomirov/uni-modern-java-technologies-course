package bg.sofia.uni.fmi.mjt.authapp.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChannelWriter implements Runnable {
    private static final String WRITE_CHANNEL_EXCEPTION_MESSAGE =
            "Something went wrong while trying to write to channel";
    private static final int BUFFER_CAPACITY = 1024;
    private static final String DISCONNECT_MESSAGE = "Disconnecting from server...";
    private SocketChannel socketChannel;
    private Scanner scanner;

    public ChannelWriter(SocketChannel socketChannel,
                         Scanner scanner) {
        this.socketChannel = socketChannel;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_CAPACITY);
        String line;
        while (socketChannel.isOpen()) {
            line = scanner.nextLine();
            // Send the message
            writeBuffer.clear();
            writeBuffer.put(line.getBytes());
            writeBuffer.flip();
            try {
                socketChannel.write(writeBuffer);
                if (line.equals("disconnect")) {
                    System.out.println(DISCONNECT_MESSAGE);
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(WRITE_CHANNEL_EXCEPTION_MESSAGE, e);
            }
        }
    }
}
