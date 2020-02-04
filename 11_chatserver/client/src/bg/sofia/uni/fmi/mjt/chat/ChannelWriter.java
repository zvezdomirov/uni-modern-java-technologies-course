package bg.sofia.uni.fmi.mjt.chat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChannelWriter implements Runnable {
    public static final String WRITE_CHANNEL_EXCEPTION_MESSAGE =
            "Something went wrong while trying to write to channel";
    private static final int BUFFER_CAPACITY = 1024;
    private static final String DISCONNECT_MESSAGE = "Disconnected from server";
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
        while (this.socketChannel.isOpen()) {
            line = this.scanner.nextLine();
            // Send the message
            writeBuffer.clear();
            writeBuffer.put(line.getBytes());
            writeBuffer.flip();
            try {
                this.socketChannel.write(writeBuffer);
                if (line.equals("disconnect")) {
                    System.out.println(DISCONNECT_MESSAGE);
                    break;
                }
            } catch (IOException e) {
                System.out.println(WRITE_CHANNEL_EXCEPTION_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
