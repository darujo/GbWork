package com.darujo.network;

import com.darujo.command.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class ClientHandler {

    private boolean connected;

    private final Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Set<ReaderMessage> listeners;


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
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Command obj = readCommand();
                    for (ReaderMessage listener : listeners) {
                        listener.processMessage(this, obj);
                    }

                }
            } catch (IOException e) {
                network.printErrorLog(NetError.DISCONNECT);
                e.printStackTrace();
            } finally {
                try {
                    close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public synchronized void sendCommand(Command command) throws IOException {
        if (connected) {

            try {
                outputStream.writeObject(command);
            } catch (IOException e) {
                network.printErrorLog(NetError.SEND_MESSAGE);
                e.printStackTrace();
                throw e;
            }
        }
    }

    private void close() throws IOException {
        System.out.println("Закрываем соединение. " + this);
        Network.getNetwork().removeClientHandler(this);
        connected = false;
        inputStream.close();
        outputStream.close();
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            network.printErrorLog(NetError.BAD_OBJECT);
            e.printStackTrace();
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