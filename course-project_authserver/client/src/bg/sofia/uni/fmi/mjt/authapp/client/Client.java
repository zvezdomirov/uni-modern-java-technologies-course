package bg.sofia.uni.fmi.mjt.authapp.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client implements Runnable {
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_HOST =
            "localhost";
    private static final String SERVER_CONNECTION_EXCEPTION_MESSAGE =
            "Could not connect to the server";
    private static final String CONNECTED_MESSAGE =
            "Connected to the server";

    public static void main(String[] args) {
        new Client().run();
    }

    public void run() {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            socketChannel.configureBlocking(false);
            System.out.println(CONNECTED_MESSAGE);
            Thread write = new Thread(
                    new ChannelWriter(socketChannel, scanner));
            Thread read = new Thread(
                    new ChannelReader(socketChannel, write));
            write.start();
            read.start();
            write.join();
        } catch (IOException ioe) {
            System.out.println(SERVER_CONNECTION_EXCEPTION_MESSAGE);
            ioe.printStackTrace();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            ie.printStackTrace();
        }
    }
}
