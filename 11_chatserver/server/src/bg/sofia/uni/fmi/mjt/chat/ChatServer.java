package bg.sofia.uni.fmi.mjt.chat;

import static bg.sofia.uni.fmi.mjt.chat.MessageConstants.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ChatServer implements Runnable {
    private static final int SERVER_PORT = 8080;
    private static final int BUFFER_CAPACITY = 1024;
    private static final int SLEEP_MILLIS = 200;
    private static final String SERVER_HOST =
            "localhost";
    private Map<String, SocketChannel> channelByName;
    private Map<SocketChannel, String> nameByChannel;
    private ByteBuffer buffer;
    private Selector selector;

    public ChatServer() {
        this.channelByName = new ConcurrentHashMap<>();
        this.nameByChannel = new ConcurrentHashMap<>();
        this.buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            System.out.println(SELECTOR_OPEN_EXCEPTION_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer().run();
    }

    @Override
    public void run() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(
                    new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            System.out.println(SERVER_STARTED_MESSAGE);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int readyChannels = this.selector.select();
                if (readyChannels == 0) {
                    System.out.println(NO_READY_CHANNELS_MESSAGE);
                    Thread.sleep(SLEEP_MILLIS);
                } else {
                    this.handleReadyChannels();
                }
            }
        } catch (IOException ioe) {
            System.out.println(SERVER_ERROR_MESSAGE);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            ie.printStackTrace();
        }
    }

    private void handleReadyChannels() throws IOException {
        Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
        Iterator<SelectionKey> keysIter = selectionKeys.iterator();
        while (keysIter.hasNext()) {
            SelectionKey currentKey = keysIter.next();
            if (currentKey.isReadable()) {
                this.handleReadableKey(currentKey);
            } else if (currentKey.isAcceptable()) {
                this.handleAcceptableKey(currentKey);
            }
            keysIter.remove();
        }
    }

    private void handleReadableKey(SelectionKey key) throws IOException {
        final SocketChannel currentChannel = (SocketChannel) key.channel();
        this.buffer.clear();
        currentChannel.read(this.buffer);
        this.buffer.flip();
        final String clientRequest = new String(buffer.array(), 0, buffer.limit());
        final String currentUsername = this.nameByChannel.get(currentChannel);
        this.processClientRequest(currentChannel, clientRequest, currentUsername);
    }

    private void processClientRequest(SocketChannel currentChannel,
                                      String clientRequest,
                                      String currentUsername) throws IOException {
        if (clientRequest.length() == 0 ||
                clientRequest.equals("disconnect")) {
            if (currentUsername != null) {
                this.nameByChannel.remove(currentChannel);
                this.channelByName.remove(currentUsername);
            }
            currentChannel.close();
        } else {
            final String[] tokens = clientRequest.split("\\s+");

            // Check if current user isn't already registered
            String command = tokens[0];
            this.interpretCommand(currentChannel, currentUsername, tokens, command);
        }
    }

    private void interpretCommand(SocketChannel currentChannel,
                                  String currentUsername,
                                  String[] tokens,
                                  String command) throws IOException {
        switch (command) {
            case "nick":
                if (currentUsername != null) {
                    this.writeToSocketChannel(currentChannel, ALREADY_LOGGED_IN_MESSAGE);
                } else {
                    String newUsername = tokens[1];
                    if (this.channelByName.containsKey(newUsername)) {
                        this.writeToSocketChannel(currentChannel,
                                MessageFormat.format(
                                        USERNAME_ALREADY_TAKEN_MESSAGE_TEMPLATE,
                                        newUsername));
                    } else {
                        this.channelByName.put(newUsername, currentChannel);
                        this.nameByChannel.put(currentChannel, newUsername);
                        this.writeToSocketChannel(currentChannel,
                                MessageFormat.format(SUCCESSFUL_REGISTRATION_MESSAGE_TEMPLATE,
                                        newUsername));
                    }
                }
                break;
            case "send": {
                final String receiverName = tokens[1];
                final String message = Arrays.stream(tokens)
                        .skip(2)
                        .collect(Collectors.joining(" "));
                if (currentUsername == null) {
                    this.writeToSocketChannel(
                            currentChannel,
                            NOT_LOGGED_IN_MESSAGE);
                } else if (!this.channelByName.containsKey(receiverName)) {
                    this.writeToSocketChannel(
                            currentChannel,
                            MessageFormat.format(
                                    USER_NOT_FOUND_MESSAGE_TEMPLATE,
                                    receiverName));
                } else {
                    final String formattedMessage = MessageFormat.format(
                            CHAT_MESSAGE_TEMPLATE,
                            currentUsername,
                            message);
                    this.writeToSocketChannel(
                            this.channelByName.get(receiverName), formattedMessage);
                    this.writeToSocketChannel(currentChannel, formattedMessage);
                }
                break;
            }
            case "send-all": {
                String message = Arrays.stream(tokens)
                        .skip(1)
                        .collect(Collectors.joining(" "));
                for (SocketChannel userChannel : this.channelByName.values()) {
                    this.writeToSocketChannel(
                            userChannel,
                            MessageFormat.format(
                                    "TO-ALL: " + CHAT_MESSAGE_TEMPLATE,
                                    currentUsername,
                                    message));
                }
                break;
            }
            case "list-users":
                this.writeToSocketChannel(
                        currentChannel,
                        String.join(", ", this.channelByName.keySet()));
                break;
            default:
                this.writeToSocketChannel(
                        currentChannel, INVALID_COMMAND_MESSAGE);
        }
    }

    private void handleAcceptableKey(SelectionKey key) throws IOException {
        final ServerSocketChannel serverSocketChannel =
                (ServerSocketChannel) key.channel();
        final SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

    private void writeToSocketChannel(SocketChannel channel, String message)
            throws IOException {
        this.buffer.clear();
        this.buffer.put(message
                .getBytes());
        this.buffer.flip();
        channel.write(this.buffer);
    }
}
