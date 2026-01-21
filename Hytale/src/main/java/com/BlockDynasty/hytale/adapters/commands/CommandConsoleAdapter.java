package com.BlockDynasty.hytale.adapters.commands;

import com.BlockDynasty.hytale.adapters.MessageAdapter;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;

import java.util.UUID;

public class CommandConsoleAdapter implements IEntityCommands {
    private ConsoleSender console;

    public  CommandConsoleAdapter(ConsoleSender console) {
        this.console = console;
    }

    @Override
    public IEntityGUI asEntityGUI() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public String getName() {
        return "BlockDynasty";
    }

    @Override
    public boolean hasPermission(String s) {
        return true;
    }

    @Override
    public void playNotificationSound() {

    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public void kickPlayer(String s) {

    }

    @Override
    public void sendMessage(String s) {
        console.sendMessage(MessageAdapter.formatVanillaMessage(s));
    }

    @Override
    public Object getRoot() {
        return console;
    }
}
