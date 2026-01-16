package com.BlockDynasty.hytale.adapters;

import abstractions.platform.IConsole;
import abstractions.platform.IProxySubscriber;
import abstractions.platform.recipes.RecipeInventory;
import abstractions.platform.recipes.RecipeItem;
import abstractions.platform.scheduler.IScheduler;
import com.BlockDynasty.hytale.adapters.Materials.MaterialAdapter;
import com.BlockDynasty.hytale.adapters.sheduler.SchedulerAdapter;
import com.BlockDynasty.hytale.adapters.textInput.ChatInput;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.currency.RecipeItemCurrency;
import domain.entity.platform.HardCashCreator;
import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import lib.gui.components.ITextInput;
import platform.IPlatform;
import platform.IPlayer;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class HytaleAdapter implements IPlatform {
    private static Universe universe= Universe.get();

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
        recipeInventory.getRows(); //tamaño
        recipeInventory.getTitle(); //titulo
        //create a cintainer indexado
        int size = recipeInventory.getRows() * 9;
        Inventory inventory = new Inventory((short)size, (short) 0, (short)0, (short)0, (short)0 );

        return new InventoryAdapter(inventory);
    }

    @Override
    public ITextInput getTextInput() {
        return new ChatInput();
    }

    @Override
    public IPlayer getPlayer(String s) {
        //obtener jugador por nombre
        PlayerRef p = universe.getPlayer(s, NameMatching.EXACT);
        return p != null ? new PlayerAdapter(p) : null;
    }

    @Override
    public void dispatchCommand(String s) throws Exception {

    }

    @Override
    public HardCashCreator asPlatformHardCash() {
        return this;
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        //obtener jugador por uuid
        PlayerRef p= universe.getPlayer(uuid);
        return p != null ? new PlayerAdapter(p) : null;
    }

    @Override
    public List<abstractions.platform.entity.IPlayer> getOnlinePlayers() {
        //obtener todos los jugadores en linea
        List<PlayerRef> playerRefs = universe.getPlayers();
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
        return false;
    }
}
