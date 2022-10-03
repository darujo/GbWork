package com.darujo.comand;

import com.darujo.comand.commanddata.DirListCommandData;
import com.darujo.comand.commanddata.GetDirListCommandData;
import com.darujo.comand.commanddata.MessageCommandData;
import com.darujo.comand.commanddata.SendFileCommandData;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Command implements Serializable{
    private final Serializable data;
    private final CommandType type;

    private Command(CommandType type, Serializable data) {
        this.data = data;
        this.type = type;
    }

    public Serializable getData() {
        return data;
    }

    public CommandType getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + this.data.toString();
    }

    public static Command getCommandSendFile (String fileName) throws IOException {
        return new Command(CommandType.SendFile, new SendFileCommandData(fileName));
    }
    public static Command getCommandErrorMessage(String text) {
        return new Command(CommandType.ERROR_MESSAGE, new MessageCommandData(text));
    }
    public static Command getCommandMessage(CommandType type, String text) {
        return new Command(type, new MessageCommandData(text));
    }
    public static Command getCommandGetDirList( String dirName) {
        return new Command(CommandType.GET_DIR_LIST, new GetDirListCommandData(dirName));
    }
    public static Command getCommandDirList(String dirName, File[] files) {
        return new Command(CommandType.DIR_LIST, new DirListCommandData(dirName,files));
    }

}
