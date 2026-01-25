package com.blockdynasty.economy.platform;

import abstractions.platform.PlatformAdapter;
import domain.entity.platform.HardCashCreator;
import lib.commands.PlatformCommand;
import lib.gui.components.PlatformGUI;

public interface IPlatform extends PlatformGUI, HardCashCreator, PlatformCommand ,PlatformAdapter{
}
