package com.darujo.networkstorageclient.handler;

import com.darujo.comand.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.function.Consumer;

public class MessageHandler extends SimpleChannelInboundHandler<Command> {
    private final Command command;
    private final Consumer<String> callback;

    public MessageHandler(Command command, Consumer<String> callback) {
        this.command = command;
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext)  {
        channelHandlerContext.writeAndFlush(command);
        System.out.println("отправка");

    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Command o)  {
        System.out.println("получено");
        callback.accept(o.toString());
//        channelHandlerContext.write(new MessageTest("ответ" ,"тест " + o.getMessage() + " " + o.getName()));

    }
}
