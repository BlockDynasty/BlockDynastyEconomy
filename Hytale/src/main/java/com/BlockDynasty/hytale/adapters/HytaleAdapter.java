package com.BlockDynasty.hytale.adapters;

import abstractions.platform.IConsole;
import abstractions.platform.IProxySubscriber;
import abstractions.platform.recipes.RecipeInventory;
import abstractions.platform.recipes.RecipeItem;
import abstractions.platform.scheduler.IScheduler;
import com.BlockDynasty.hytale.adapters.Materials.MaterialAdapter;
import com.BlockDynasty.hytale.adapters.sheduler.SchedulerAdapter;
import com.BlockDynasty.hytale.adapters.textInput.ChatInput;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.RecipeItemCurrency;
import domain.entity.platform.HardCashCreator;
import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import lib.gui.components.ITextInput;
import platform.IPlatform;
import platform.IPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


public class HytaleAdapter implements IPlatform {
    private static Map<UUID, PlayerRef> playerUUIDCache = new HashMap<>();
    private static Map<String, PlayerRef> playerNameCache = new HashMap<>();

    public static void connectPlayer(PlayerRef playerRef){
        playerUUIDCache.put(playerRef.getUuid(), playerRef);
        playerNameCache.put(playerRef.getUsername(), playerRef);
    }

    public static void disconnectPlayer(PlayerRef playerRef){
        playerUUIDCache.remove(playerRef.getUuid());
        playerNameCache.remove(playerRef.getUsername());
    }

    @Override
    public void sendPluginMessage(String s, byte[] bytes) {

    }

    @Override
    public void registerMessageChannel(IProxySubscriber iProxySubscriber) {

    }

    @Override
    public IScheduler getScheduler() {
        return new SchedulerAdapter();
    }

    @Override
    public IConsole getConsole() {
        return new ConsoleAdapter();
    }

    @Override
    public File getDataFolder() {
        return new File("mods/DynastyEconomy");
    }

    @Override
    public boolean isLegacy() {
        return true;
    }

    @Override
    public boolean isOnlineMode() {
        return true;
    }

    @Override
    public boolean hasSupportAdventureText() {
        return false;
    }

    @Override
    public IItemStack createItemStack(RecipeItem recipeItem) {
        return MaterialAdapter.createItemStack(recipeItem);
    }

    @Override
    public IInventory createInventory(RecipeInventory recipeInventory) {
        return new InventoryAdapter(recipeInventory);
    }

    @Override
    public ITextInput getTextInput() {
        return new ChatInput();
    }

    @Override
    public IPlayer getPlayer(String s) {
        PlayerRef p = playerNameCache.get(s);
        return p != null ? new PlayerAdapter(p) : null;
    }

    @Override
    public void dispatchCommand(String s) throws Exception {
        CommandManager.get().handleCommand(ConsoleSender.INSTANCE,s);
    }

    @Override
    public HardCashCreator asPlatformHardCash() {
        return this;
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        PlayerRef p= playerUUIDCache.get(uuid);
        return p != null ? new PlayerAdapter(p) : null;
    }

    @Override
    public List<abstractions.platform.entity.IPlayer> getOnlinePlayers() {
        List<PlayerRef> playerRefs = playerNameCache.values().stream().collect(Collectors.toList());
        return playerRefs.stream().map(PlayerAdapter::new).collect(Collectors.toList());
    }

    @Override
    public ItemStackCurrency createItemStackCurrency(RecipeItemCurrency recipeItemCurrency) {
        return MaterialAdapter.createItemStack(recipeItemCurrency);
    }

    @Override
    public boolean hasSupportHardCash() {
        return false;
    }

    @Override
    public boolean hasSupportGui() {
        return true;
    }
}
