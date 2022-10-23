package com.darujo.networkstorageserver.handler;

import com.darujo.command.Command;
import com.darujo.command.CommandType;
import com.darujo.command.commanddata.AuthCommandData;
import com.darujo.command.commanddata.RegistrationUser;
import com.darujo.networkstorageserver.auchcenter.AuthCenter;
import com.darujo.networkstorageserver.auchcenter.User;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class AuchHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  {
        if (msg instanceof Command command) {
            ChannelFuture future = null;
            if (command.getType() == CommandType.AUTH) {
                future = ctx.writeAndFlush(authAndSendAnswer((AuthCommandData) command.getData()));
            } else if (command.getType() == CommandType.REGISTRATION_USER) {
                future = ctx.writeAndFlush(registrationAndSendAnswer((RegistrationUser) command.getData()));
//            } else if (command.getType() == CommandType.USER_DATA_CHANGE) {
//                future = ctx.writeAndFlush(changeUserData(clientHandler,(ChangeUserData) command.getData()));
//            }
            }else{

                if (AuthCenter.getInstance().checkToken(command.getToken())){
                    ctx.fireChannelRead(msg);
                } else {
                    future = ctx.writeAndFlush(Command.getErrorMessageCommand(CommandType.FAILED_TOKEN,"Токен просрочен, необходима повторная авторизация"));
                }
            }

            if (future !=null) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }
    private Command authAndSendAnswer( AuthCommandData data) {
        AuthCenter authCenter = AuthCenter.getInstance();
        AuthCenter.AuthMessage authMessage = authCenter.availableUser(data.getLogin(), data.getPassword());
        User user = authCenter.getUser(data.getLogin());
        return authSendAnswer(user, authMessage);
    }
    private Command registrationAndSendAnswer( RegistrationUser data)  {
        AuthCenter authCenter = AuthCenter.getInstance();
        AuthCenter.AuthMessage authMessage = authCenter.registrationUser(data.getLogin(), data.getPassword(), data.getUserName());
        User user = authCenter.getUser(data.getLogin());
        return authSendAnswer( user, authMessage);
    }
    private Command authSendAnswer( User user, AuthCenter.AuthMessage authMessage)  {
        if (authMessage == AuthCenter.AuthMessage.AUTH_OK) {
            return Command.getAuthOkCommand(user.getUserPublic());
//        } else if(authMessage == AuthCenter.AuthMessage.USER_DATA_CHANGE_OK) {
//            return Command.getChangeUserDataOkCommand(user.getUserPublic());
        } else if (authMessage == AuthCenter.AuthMessage.INVALID_LOGIN) {
            return Command.getAuthNoUserCommand(authMessage.getMessage());
        } else {
            CommandType commandType;
            if (authMessage == AuthCenter.AuthMessage.LOGIN_IS_BUSY) {
                commandType = CommandType.LOGIN_IS_BUSY;
            } else if (authMessage == AuthCenter.AuthMessage.USER_NAME_IS_BUSY) {
                commandType = CommandType.USER_NAME_IS_BUSY;
            } else {
              commandType = CommandType.ERROR_MESSAGE;
            }
            return Command.getErrorMessageCommand(commandType, authMessage.getMessage());
        }
    }


}
