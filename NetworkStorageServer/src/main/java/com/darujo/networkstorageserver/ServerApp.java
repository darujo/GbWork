package com.darujo.networkstorageserver;

import com.darujo.networkstorageserver.auchcenter.AuthCenter;
import com.darujo.networkstorageserver.handler.AuchHandler;
import com.darujo.networkstorageserver.handler.FileWorkingHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ServerApp {
    private final static int MAX_OBJECT_SIZE = 1024*1024*100;
    private final int port;

    public ServerApp(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new ServerApp(9000).run();
    }

    private void run() throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup);
            server.channel(NioServerSocketChannel.class);
            server.option(ChannelOption.SO_BACKLOG, 128);
            server.childOption(ChannelOption.SO_KEEPALIVE, true);
            server.childHandler(new ChannelInitializer<>()  {
                @Override
                protected void initChannel(Channel ch)  {
                    ch.pipeline().addLast(
                            new ObjectEncoder(),
                            new ObjectDecoder(MAX_OBJECT_SIZE, ClassResolvers.cacheDisabled(null)),
                            new AuchHandler(),
                            new FileWorkingHandler()
                    );
                }
            });
            ChannelFuture future = server.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            AuthCenter.close();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
