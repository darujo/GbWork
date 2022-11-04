package com.darujo.networkstorageclient.handler;

import com.darujo.command.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.function.Consumer;

public class MessageHandler extends SimpleChannelInboundHandler<Command> {
    private final Command command;
    private final Consumer<Command> callback;

    public MessageHandler(Command command, Consumer<Command> callback) {
        this.command = command;
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext)  {
        System.out.println("отправка " + command );
        channelHandlerContext.writeAndFlush(command);
        System.out.println("отправлена " + command);

    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Command o)  {
        System.out.println("получено " + o);
        callback.accept(o);
//        channelHandlerContext.write(new MessageTest("ответ" ,"тест " + o.getMessage() + " " + o.getName()));

    }
}
