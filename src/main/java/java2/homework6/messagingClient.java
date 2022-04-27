package java2.homework6;

public class messagingClient {

    public static void main(String[] args) {
        Network network = new Network();
        network.createSocketClient();
        network.startExchange();
    }
}