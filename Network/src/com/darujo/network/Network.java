package com.darujo.network;

import com.darujo.event.Event;
import com.darujo.event.EventType;
import com.darujo.event.ReaderEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Network {

    private static final String SERVER_ADR = "localhost";
    private static final int SERVER_PORT = 8189;
    public ErrorPrinter logErrorPrinter;
    private static Socket socket;
    private final Set<ReaderMessage> messageListeners = new CopyOnWriteArraySet<>();
    private ClientHandler lastClientHandler;
    private final Set<ClientHandler> clientHandlers = new HashSet<>();
    private final Set<ReaderEvent> eventListeners = new HashSet<>();

    private static Network instance;

    private Network() {
        instance = this;
    }


    public void createSocketServer() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Сервер запущен.");
            while (true) {
                System.out.println("Ожидаем подключения.");
                socket = serverSocket.accept();
                System.out.println("Клиент подключился. " + workSocket(false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientHandler createSocketClient() {
        try {
            socket = new Socket(SERVER_ADR, SERVER_PORT);
            System.out.println("Сооединение с " + SERVER_ADR + ":" + SERVER_PORT + " установлено.");
            lastClientHandler = workSocket(false);
            return lastClientHandler;
        } catch (IOException e) {
            printErrorLog(NetError.SERVER_CONNECT, SERVER_ADR + ":" + SERVER_PORT);

        }
        return null;
    }

    private ClientHandler workSocket(boolean isServer) throws IOException {
        ClientHandler clientHandler = new ClientHandler(socket, instance, messageListeners);
        clientHandler.handle(isServer);
        clientHandler.readSocket();
        return clientHandler;
    }

    public void printErrorLog(NetError netError) {
        printErrorLog(netError, "");
    }

    public void printErrorLog(NetError netError, String afterText) {
        String text = netError.getMessage(afterText);
        if (logErrorPrinter != null) {
            logErrorPrinter.print(netError, text);
        }
        System.out.println(text);

    }

    public static Network getNetwork() {
        if (instance == null) {
            return new Network();
        }
        return instance;
    }

    public static Network getNetwork(ErrorPrinter logErrorPrinter) {
        if (instance == null) {
            instance = new Network();
        }
        instance.logErrorPrinter = logErrorPrinter;
        return instance;
    }

    public synchronized void addReaderMessage(ReaderMessage listener) {
        this.messageListeners.add(listener);
    }

    public synchronized void removeReaderMessage(ReaderMessage listener) {
        this.messageListeners.remove(listener);
    }

    public synchronized void addClientHandler(ClientHandler clientHandler) {
        this.clientHandlers.add(clientHandler);
        publicEvent(new Event(EventType.ADD_CLIENT_HANDLER, clientHandler));
    }

    public synchronized void removeClientHandler(ClientHandler clientHandler) {
        this.clientHandlers.remove(clientHandler);
        publicEvent(new Event(EventType.REMOVE_CLIENT_HANDLER, clientHandler));
    }

    public void publicEvent(Event eventObj) {
        for (ReaderEvent eventListener : eventListeners) {
            eventListener.processEvent(eventObj);
        }
    }

    public synchronized void addReaderEvent(ReaderEvent listener) {
        this.eventListeners.add(listener);
    }

    public synchronized void removeReaderEvent(ReaderEvent listener) {
        this.eventListeners.remove(listener);
    }

    public ClientHandler getLastClientHandler() {
        return lastClientHandler;
    }
}
