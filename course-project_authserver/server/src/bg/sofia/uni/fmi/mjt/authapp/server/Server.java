package bg.sofia.uni.fmi.mjt.authapp.server;

import bg.sofia.uni.fmi.mjt.authapp.exceptions.OpType;
import bg.sofia.uni.fmi.mjt.authapp.exceptions.SocketChannelException;
import bg.sofia.uni.fmi.mjt.authapp.requesthandler.RequestHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_HOST = "localhost";
    private static final String SERVER_START_EXCEPTION_MESSAGE =
            "Something went wrong with the server";
    private static final String SERVER_STARTED_MESSAGE =
            "Server started listening on port 8080";

    private RequestHandler requestHandler;

    public Server() {
        requestHandler = new RequestHandler();
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(
                    new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println(SERVER_STARTED_MESSAGE);

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keysIter = selectionKeys.iterator();

                while (keysIter.hasNext()) {
                    SelectionKey currentKey = keysIter.next();
                    if (currentKey.isReadable()) {
                        handleReadableKey(currentKey);
                    } else if (currentKey.isAcceptable()) {
                        handleAcceptableKey(currentKey, selector);
                    }
                    keysIter.remove();
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException(SERVER_START_EXCEPTION_MESSAGE);
        }
    }

    private void handleReadableKey(SelectionKey key) {
        SocketChannel currentChannel = (SocketChannel) key.channel();
        String clientCommand = requestHandler.readFromSocketChannel(currentChannel);
        interpretClientCommand(currentChannel, clientCommand);
    }

    private void handleAcceptableKey(SelectionKey key, Selector selector) {
        ServerSocketChannel serverSocketChannel =
                (ServerSocketChannel) key.channel();
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new SocketChannelException(OpType.REGISTER);
        }
    }

    private void interpretClientCommand(SocketChannel currentChannel, String clientCommand) {
        if (clientCommand.matches(CommandFormat.REGISTER.getPattern())) {
            requestHandler.registerUser(currentChannel, clientCommand);
        } else if (clientCommand.matches(CommandFormat.LOGIN.getPattern())) {
            requestHandler.loginUser(currentChannel, clientCommand);
        } else if (clientCommand.matches(CommandFormat.UPDATE_USER.getPattern())) {
            requestHandler.updateUser(currentChannel, clientCommand);
        } else if (clientCommand.matches(CommandFormat.RESET_PASSWORD.getPattern())) {
            requestHandler.resetPassword(currentChannel, clientCommand);
        } else if (clientCommand.matches(CommandFormat.ADD_ADMIN_USER.getPattern())) {
            requestHandler.addAdminUser(currentChannel, clientCommand);
        } else if (clientCommand.matches(CommandFormat.REMOVE_ADMIN_USER.getPattern())) {
            requestHandler.removeAdminUser(currentChannel, clientCommand);
        } else if (clientCommand.matches(CommandFormat.DELETE_USER.getPattern())) {
            requestHandler.deleteUser(currentChannel, clientCommand);
        } else if (clientCommand.matches(CommandFormat.LOGOUT.getPattern())) {
            requestHandler.logoutUser(currentChannel, clientCommand);
        } else if (clientCommand.matches(CommandFormat.DISCONNECT.getPattern())) {
            requestHandler.disconnectFromServer(currentChannel);
        } else {
            requestHandler.handleWrongCommand(currentChannel);
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}