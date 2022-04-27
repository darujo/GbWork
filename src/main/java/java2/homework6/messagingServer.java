package java2.homework6;

public class messagingServer {
    public static void main(String[] args) {
        Network network = new Network();
        network.createSocketServer();
        network.startExchange();
    }
}
