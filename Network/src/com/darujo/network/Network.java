package com.darujo.network;

import com.darujo.event.Event;
import com.darujo.event.EventType;
import com.darujo.event.ReaderEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Network {

    private static final String SERVER_ADR = "localhost";
    private static final int SERVER_PORT = 8189;
    public ErrorPrinter logErrorPrinter;
    private static Socket socket;
    private final Set<ReaderMessage> messageListeners = new CopyOnWriteArraySet<>();
    private ClientHandler lastClientHandler;
    private final Set<ClientHandler> clientHandlers = new HashSet<>();
    private final Set<ReaderEvent> eventListeners = new HashSet<>();
    private final Logger LOGGER = LogManager.getLogger(Network.class);
    private static Network instance;

    public final ExecutorService executorService;
    private Network() {
        instance = this;
        executorService = Executors.newCachedThreadPool();
    }

    public void close() {
        executorService.shutdownNow();
        for (ClientHandler clientHandler : clientHandlers){
            try {
                clientHandler.close();
            } catch (IOException e) {
                printErrorLog(NetError.DISCONNECT,e.getMessage());
            }
        }
    }


    public void createSocketServer() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            LOGGER.info("Сервер запущен.");
            while (true) {
                LOGGER.info("Ожидаем подключения.");
                socket = serverSocket.accept();
                LOGGER.info("Клиент подключился. {}", workSocket(false));
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(e.getStackTrace());
        }
    }

    public ClientHandler createSocketClient() {
        try {
            socket = new Socket(SERVER_ADR, SERVER_PORT);
            LOGGER.info("Сооединение с {}: {} установлено.", SERVER_ADR, SERVER_PORT);
            lastClientHandler = workSocket(true);
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
        addClientHandler(clientHandler);
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
        LOGGER.warn(text);

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
