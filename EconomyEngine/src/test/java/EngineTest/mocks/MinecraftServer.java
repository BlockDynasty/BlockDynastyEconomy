package EngineTest.mocks;

import Main.Economy;
import com.BlockDynasty.api.DynastyEconomy;

import java.util.ArrayList;
import java.util.List;

public class MinecraftServer {
    private static final List<Player> onlinePlayers = new ArrayList<>();
    private static Economy economy;
    private static final boolean onlineMode= true;

    private MinecraftServer(){
    }

    public static void start(){
        System.out.println("Server Testing Environment Initialized.");
        if (economy == null){
            economy = Economy.init( new EngineTest.mocks.Platform());
        }
    }

    public static void stop(){
        //Economy.shutdown();
        System.out.println("Server Testing Environment Stopped.");
    }

    public static DynastyEconomy getApi(){
        return economy.getApi();
    }

    public static Economy getEconomy(){
        return economy;
    }

    public static void connectPlayer(Player player){
        onlinePlayers.add(player);
        getEconomy().getPlayerJoinListener().loadPlayerAccount(player);
    }

    public static void disconnectPlayer(Player player){
        onlinePlayers.remove(player);
        getEconomy().getPlayerJoinListener().offLoadPlayerAccount(player);
    }

    public static boolean isOnline(Player player){
        return onlinePlayers.contains(player);
    }

    public static Player getPlayer(String playerName){
            return onlinePlayers.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(playerName))
                    .findFirst()
                    .orElse(null);
    }

    public static List<Player> getOnlinePlayers(){
        return onlinePlayers;
    }


}
