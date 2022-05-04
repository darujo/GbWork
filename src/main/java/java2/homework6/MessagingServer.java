package java2.homework6;

public class MessagingServer {
    public static void main(String[] args) {
        Network network = new Network();
        network.createSocketServer();
        network.startExchange();
    }
}
