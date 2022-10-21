package com.darujo.networkstorageserver.handler;

import com.darujo.comand.Command;
import com.darujo.comand.CommandType;
import com.darujo.comand.commanddata.GetDirListCommandData;
import com.darujo.comand.commanddata.SendFileCommandData;
import com.darujo.networkstorageserver.fileworker.FileWork;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;


public class FileWorkingHandler extends SimpleChannelInboundHandler<Command> {

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext)  {
        System.out.println("подключен");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Command command)  {
        System.out.println("отвечаем");
        Command answerCommand = null;
        if (command.getType() == CommandType.SendFile) {
            answerCommand = FileWork.saveFile((SendFileCommandData) command.getData());
        } else if(command.getType() == CommandType.GET_DIR_LIST){
            answerCommand = FileWork.getDirList(((GetDirListCommandData) command.getData()).getDirName());
        }

        System.out.println("отвечаем");

        ChannelFuture future;
        if (answerCommand != null) {
           future =channelHandlerContext.writeAndFlush(answerCommand);
        } else {
            future =channelHandlerContext.writeAndFlush(Command.getCommandMessage(CommandType.ERROR_MESSAGE,"Пустая команда ответа"));
        }
        future.addListener(ChannelFutureListener.CLOSE);
    }


}
