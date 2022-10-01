package com.darujo.networkstorageclient.handler;

import com.darujo.MessageTest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.function.Consumer;

public class MessageHandler extends SimpleChannelInboundHandler<MessageTest> {
    private final MessageTest message;
    private final Consumer<String> callback;

    public MessageHandler(MessageTest message, Consumer<String> callback) {
        this.message = message;
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext)  {
        System.out.println("отправка");
        channelHandlerContext.writeAndFlush(message);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, MessageTest o)  {
        System.out.println("получено");
        callback.accept(o.toString());
//        channelHandlerContext.write(new MessageTest("ответ" ,"тест " + o.getMessage() + " " + o.getName()));

    }
}
