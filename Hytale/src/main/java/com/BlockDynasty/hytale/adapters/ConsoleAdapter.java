package com.BlockDynasty.hytale.adapters;

import abstractions.platform.IConsole;
import com.hypixel.hytale.logger.HytaleLogger;

public class ConsoleAdapter implements IConsole {
    private static HytaleLogger logger = HytaleLogger.getLogger();
    private static final String Debug_Prefix = "[BlockDynastyEconomy-Debug] ";
    private static final String Console_Prefix = "[BlockDynastyEconomy] ";
    private static final String Error_Prefix = "[BlockDynastyEconomy-Error] ";

    @Override
    public void debug(String s) {
       System.out.println(Debug_Prefix + s);
        //logger.atInfo().log(Debug_Prefix + s);
    }

    @Override
    public void log(String s) {
        System.out.println(Console_Prefix + s);
        //logger.atInfo().log(Console_Prefix + s);
    }

    @Override
    public void logError(String s) {
        System.out.println(Error_Prefix + s);
        //logger.atInfo().log(Error_Prefix + s);
    }

}
