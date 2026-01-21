package com.BlockDynasty.hytale.adapters;

import abstractions.platform.IConsole;
import com.hypixel.hytale.logger.HytaleLogger;

public class ConsoleAdapter implements IConsole {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final String Debug_Prefix = "[BlockDynastyEconomy-Debug] ";
    private static final String Console_Prefix = "[BlockDynastyEconomy] ";
    private static final String Error_Prefix = "[BlockDynastyEconomy-Error] ";

    @Override
    public void debug(String s) {
        LOGGER.atInfo().log(Debug_Prefix + s);
    }

    @Override
    public void log(String s) {
        LOGGER.atInfo().log(Console_Prefix + s);
    }

    @Override
    public void logError(String s) {
        LOGGER.atInfo().log(Error_Prefix + s);
    }

}
