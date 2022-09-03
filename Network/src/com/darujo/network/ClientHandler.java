package com.darujo.network;

import com.darujo.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Set;

public class ClientHandler {

    private boolean connected;

    private final Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Set<ReaderMessage> listeners;
    private static final Logger LOGGER = LogManager.getLogger(Network.class);
    private final Network network;

    public ClientHandler(Socket clientSocket, Network network, Set<ReaderMessage> listeners) {
        this.listeners = listeners;
        this.clientSocket = clientSocket;
        this.network = network;

    }

    public void handle(Boolean isServer) throws IOException {
        if (isServer) {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } else {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        }
        connected = true;

    }

    public void readSocket() {
        Network.getNetwork().executorService.execute(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Command obj = readCommand();
                    LOGGER.info("Получена команда " + (obj != null ? obj.getType() : ""));
                    for (ReaderMessage listener : listeners) {
                        listener.processMessage(this, obj);
                    }
                }
            } catch (SocketException e) {
                if(!Thread.currentThread().isInterrupted()){
                    network.printErrorLog(NetError.READ_MESSAGE_SOCKET);
                }
            } catch (IOException e) {
                network.printErrorLog(NetError.DISCONNECT);
            } finally {
                try {
                    close(!Thread.currentThread().isInterrupted());

                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                    LOGGER.error(e.getStackTrace());
                }
            }
        });
    }

    public synchronized void sendCommand(Command command) throws IOException {
        if (connected) {

            try {
                outputStream.writeObject(command);
                LOGGER.info("Отправлена комманда " + command.getType());
            } catch (IOException e) {
                network.printErrorLog(NetError.SEND_MESSAGE);
                LOGGER.error(e.getMessage());
                LOGGER.error(e.getStackTrace());
                throw e;
            }
        }
    }

    public void close() throws IOException {
        close(false);
    }

    public void close(boolean reLoad) throws IOException {
        if (connected) {
           LOGGER.info("Закрываем соединение. " + this);
            if (reLoad) {
                Network.getNetwork().removeClientHandler(this);
            }
            inputStream.close();
            outputStream.close();
            connected = false;
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            network.printErrorLog(NetError.BAD_OBJECT);
        }
        return null;
    }


    public void addReaderMessage(ReaderMessage listener) {
        this.listeners.add(listener);
    }

    public void removeReaderMessage(ReaderMessage listener) {
        this.listeners.remove(listener);
    }
}
