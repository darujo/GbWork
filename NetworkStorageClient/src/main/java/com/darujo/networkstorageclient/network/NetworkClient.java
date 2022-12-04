package com.darujo.networkstorageclient.network;

import com.darujo.command.Command;
import com.darujo.command.object.PathFile;
import com.darujo.networkstorageclient.handler.MessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.IOException;
import java.util.function.Consumer;

public class NetworkClient {
    private final static int MAX_OBJECT_SIZE = 1024*1024*100;
    private static final String HOST = "localhost";
    private static final int PORT = 9000;

    private static NetworkClient instance;
    public NetworkClient(String host, int port) {
//        this.host = host;
//        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        String  file = "NetworkStorageClient/dir/my-test-file.txt";
//        Message message = new Message("put", file, Files.readAllBytes(file.toPath()));
//        MessageTest message =new MessageTest("test1","testing 1") ;
        Command sendFile = Command.getSendFileCommand(new PathFile(""),file);
        new NetworkClient("localhost", 9000).send(sendFile, (response) -> System.out.println("response = " + response));
        PathFile filePath = new PathFile("NetworkStorageClient/dir");
        Command listDir = Command.getGetDirListCommand(filePath);
        new NetworkClient(HOST, PORT).send(listDir, (response) -> System.out.println("response = " + response));

    }

    public void send(Command command, Consumer<Command> callback) throws InterruptedException {
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
                            new MessageHandler(command, callback)
                    );
                }
            });
            ChannelFuture future = client.connect(HOST, PORT).sync();
            future.channel().closeFuture().sync();

        }
//        catch (RuntimeException exception){
//            Dialogs.showDialog(Alert.AlertType.ERROR,"1111",null,"2222");
//        }
        finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static NetworkClient getInstance() {
        if (instance == null){
            instance = new NetworkClient(HOST,PORT);
        }
        return instance;
    }
}
