package com.darujo.network;

import com.darujo.command.Command;

public interface ReaderMessage {
    void processMessage(ClientHandler clientHandler, Command command);
}
