package java2.homework6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

public class Network {

    private static final String SERVER_ADR = "localhost";
    private static final int SERVER_PORT = 8189;


    private static Socket socket;
    private final Consumer<String> writeMessage;
    private boolean stopExchange;

    public Network() {
        this(System.out::println);
    }

    public Network(Consumer<String> writeMessage) {
        this.writeMessage = writeMessage;
    }

    public void createSocketServer() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Сервер запущен, ожидаем подключения...");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSocketClient() {
        try {
            socket = new Socket(SERVER_ADR, SERVER_PORT);
            writeMessage.accept("Сооединение с " + SERVER_ADR + ":" + SERVER_PORT + " установлено");
        } catch (IOException e) {
            writeMessage.accept("Не удалось установить соединение с " + SERVER_ADR + ":" + SERVER_PORT);
        }
    }

    public void startExchange() {
        if (socket != null) {
            try {
                sendMessage(socket);
                readSocket(socket);
                closeConnection();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() throws IOException {
        writeMessage.accept("Закрываем сооединение");
        socket.close();
    }

    private void readSocket(Socket socket) throws IOException, InterruptedException {
        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
            while (!stopExchange) {
                String str = in.readUTF();
                if (str.equalsIgnoreCase("/end")) {
                    writeMessage.accept("Получена команда на отключение");
                    break;
                }
                writeMessage.accept(str);
            }
        } finally {
            writeMessage.accept("Соединение разорвано");
            stopExchange = true;
        }
    }

    public void sendMessage(Socket socket) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Thread threadSend = new Thread(() -> {
            try (Scanner scan = new Scanner(System.in)) {

                while (true) {
                    writeMessage.accept("Введите сообщение для отправки");
                    String str = scan.nextLine();
                    if (!stopExchange) {
                        out.writeUTF(str);
                        if (str.equalsIgnoreCase("/end")) {
                            stopExchange = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        threadSend.setDaemon(true);
        threadSend.start();
    }
}
