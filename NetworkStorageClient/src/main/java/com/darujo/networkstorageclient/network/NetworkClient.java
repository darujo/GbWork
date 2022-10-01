package com.darujo.networkstorageclient.network;

import com.darujo.MessageTest;
import com.darujo.networkstorageclient.handler.MessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.function.Consumer;

public class NetworkClient {
    private final static int MAX_OBJECT_SIZE = 1024*1024*100;
    private final String host;
    private final int port;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
//        File file = new File("client/dir/my-file.txt");
//        Message message = new Message("put", file, Files.readAllBytes(file.toPath()));
        MessageTest message =new MessageTest("test1","testing 1") ;
        new NetworkClient("localhost", 9000).send(message, (response) -> System.out.println("response = " + response));
//        while (true) {
//            Scanner scanner = new Scanner(System.in);
//            if (scanner.nextLine().equals("send")) {
//                new Client("localhost", 9000).send(message, (response) -> {
//                    System.out.println("response = " + response);
//                });
//            }
//        }

    }

    private void send(MessageTest message, Consumer<String> callback) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap client = new Bootstrap();
            client.group(workerGroup);
            client.channel(NioSocketChannel.class);
            client.option(ChannelOption.SO_KEEPALIVE, true);
            client.handler(new ChannelInitializer<>() {
                @Override
                protected void initChannel(Channel ch) {
                    ch.pipeline().addLast(
                            new ObjectEncoder(),
                            new ObjectDecoder(MAX_OBJECT_SIZE, ClassResolvers.cacheDisabled(null)),
                            new MessageHandler(message, callback)
                    );
                }
            });
            ChannelFuture future = client.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
