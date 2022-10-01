package com.darujo.networkstorageserver.handler;

import com.darujo.MessageTest;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.FutureListener;

public class MessageHandler extends SimpleChannelInboundHandler<MessageTest> {
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext)  {
        System.out.println("подключен");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, MessageTest o)  {
        System.out.println("отвечаем");
        ChannelFuture future =channelHandlerContext.writeAndFlush(new MessageTest("ответ" ,"тест " + o.getMessage() + " " + o.getName()));
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
