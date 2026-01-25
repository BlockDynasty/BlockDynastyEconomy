package com.blockdynasty.economy.platform;

import domain.entity.player.IEntityHardCash;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;

public interface IPlayer extends IEntityCommands, IEntityHardCash, IEntityGUI {
}
