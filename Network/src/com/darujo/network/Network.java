package com.darujo.network;

import com.darujo.event.Event;
import com.darujo.event.EventType;
import com.darujo.event.ReaderEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class Network {

    private static final String SERVER_ADR = "localhost";
    private static final int SERVER_PORT = 8189;
    public Consumer<String> logErrorPrinter;
    public List<ReaderMessage> readerMessages = new ArrayList<>();
    private static Socket socket;
    private List<ReaderMessage> messageListeners;
    private ClientHandler lastClientHandler;
    private final List<ClientHandler> clientHandlers = new ArrayList<>();
    private final List<ReaderEvent> eventListeners = new ArrayList<>();

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
                System.out.println("Клиент подключился");
                workSocket(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientHandler createSocketClient() {
        try {
            socket = new Socket(SERVER_ADR, SERVER_PORT);
            System.out.println("Сооединение с " + SERVER_ADR + ":" + SERVER_PORT + " установлено");
            messageListeners = new CopyOnWriteArrayList<>();
            lastClientHandler = workSocket(false);
            return lastClientHandler;
        } catch (IOException e) {
            printErrorLog(NetError.SERVER_CONNECT.getMessage(SERVER_ADR + ":" + SERVER_PORT));

        }
        return null;
    }

    private ClientHandler workSocket(boolean isServer) throws IOException {
        ClientHandler clientHandler = new ClientHandler(socket, instance, messageListeners);
        clientHandler.handle(isServer);
        clientHandler.readSocket();
        for (ReaderMessage readerMessage : readerMessages) {
            clientHandler.addReaderMessage(readerMessage);
        }
        return clientHandler;
    }

    public void printErrorLog(String str) {
        if (logErrorPrinter != null) {
            logErrorPrinter.accept(str);
        }
        System.out.println(str);

    }

    public static Network getNetwork() {
        if (instance == null) {
            return new Network();
        }
        return instance;
    }

    public static Network getNetwork(Consumer<String> logErrorPrinter) {
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
