package com.darujo.networkstorageserver.handler;

import com.darujo.command.Command;
import com.darujo.command.CommandType;
import com.darujo.command.commanddata.FileNameCommandData;
import com.darujo.command.commanddata.MessageCommandData;
import com.darujo.command.commanddata.SendFileCommandData;
import com.darujo.networkstorageserver.auchcenter.AuthCenter;
import com.darujo.networkstorageserver.fileworker.FileWork;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class FileWorkingHandler extends SimpleChannelInboundHandler<Command> {

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        System.out.println("подключен");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Command command) {
        System.out.println("обработка команды " + command);
        Command answerCommand = null;
        Integer userId = AuthCenter.getInstance().getUserId(command.getToken());
        if (command.getType() == CommandType.SEND_FILE) {
            answerCommand = FileWork.saveFile(userId, (SendFileCommandData) command.getData());
        } else if (command.getType() == CommandType.GET_DIR_LIST) {
            answerCommand = FileWork.getDirList(userId, ((FileNameCommandData) command.getData()).getFilePath());
        } else if (command.getType() == CommandType.DEL_FILE) {
            answerCommand = FileWork.getDelFile(userId, (SendFileCommandData) command.getData());
        } else if (command.getType() == CommandType.GET_SEND_FILE) {
            answerCommand = FileWork.sendFile(userId, (FileNameCommandData) command.getData());
        } else if (command.getType() == CommandType.GET_NEW_GUID) {
            answerCommand = FileWork.getNewGuid(userId, (FileNameCommandData) command.getData());
        } else if (command.getType() == CommandType.ADD_GUID) {
            answerCommand = FileWork.addGuidForUser(userId, (MessageCommandData) command.getData());
        }

        System.out.println("отвечаем " + answerCommand);

        ChannelFuture future;
        if (answerCommand != null) {
            future = channelHandlerContext.writeAndFlush(answerCommand);
        } else {
            future = channelHandlerContext.writeAndFlush(Command.getErrorMessageCommand("Не известная команда " + command.getType()));
        }
        future.addListener(ChannelFutureListener.CLOSE);
    }


}
