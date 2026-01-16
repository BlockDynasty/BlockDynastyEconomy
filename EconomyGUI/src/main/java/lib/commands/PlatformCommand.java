package lib.commands;

import abstractions.platform.entity.IPlayer;
import domain.entity.platform.HardCashCreator;
import lib.gui.components.ITextInput;

public interface PlatformCommand {
    IPlayer getPlayer(String name);
    void dispatchCommand(String command) throws Exception;
    ITextInput getTextInput();
    HardCashCreator asPlatformHardCash();
}
