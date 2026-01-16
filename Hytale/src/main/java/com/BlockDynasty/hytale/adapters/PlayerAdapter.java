package com.BlockDynasty.hytale.adapters;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.player.IEntityHardCash;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IInventory;
import platform.IPlayer;

import java.util.UUID;

public class PlayerAdapter implements IPlayer {
    private PlayerRef playerRef;

    public PlayerAdapter(PlayerRef playerRef) {
        this.playerRef = playerRef;
    }

    @Override
    public UUID getUniqueId() {
        return playerRef.getUuid();
    }

    @Override
    public String getName() {
        return playerRef.getUsername();
    }

    @Override
    public boolean hasPermission(String s) {
        return  PermissionsModule.get().hasPermission(playerRef.getUuid(),s);
    }

    @Override
    public void playNotificationSound() {

    }

    @Override
    public boolean isOnline() {
        PlayerRef p = Universe.get().getPlayer(playerRef.getUuid());
        return p != null;
    }

    @Override
    public void kickPlayer(String s) {
        //implement kick logic with server API
    }

    @Override
    public void sendMessage(String s) {
        playerRef.sendMessage(MessageAdapter.formatVanillaMessage(s));
    }

    private String colorCodeToLegacy(String message) {
        return message.replaceAll("§[0-9a-fA-Fk-oK-OrR]", "");
    }

    @Override
    public Object getRoot() {
        return playerRef;
    }

    @Override
    public void giveItem(ItemStackCurrency itemStackCurrency) {
        //dar item a jugador//no implementar es de hardcash
    }

    @Override
    public ItemStackCurrency takeHandItem() {
        //hardcash no implementar
        return null;
    }

    @Override
    public boolean hasItem(ItemStackCurrency itemStackCurrency) {
        return false;
        //hardcash no implementar
    }

    @Override
    public boolean hasEmptySlot() {
        return false;
        //hardcash no implementar
    }

    @Override
    public void removeItem(ItemStackCurrency itemStackCurrency) {
        //hardcash no implementar
    }

    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public void playSuccessSound() {

    }

    @Override
    public void playFailureSound() {

    }

    //close GUI
    @Override
    public void closeInventory() {
        //todo implement
    }

    //open GUI la cual puede ser otra cosa que no sea un inventario
    @Override
    public void openInventory(IInventory iInventory) {
        //todo implement

    }

    @Override
    public IEntityCommands asEntityCommands() {
        return this;
    }

    @Override
    public IEntityHardCash asEntityHardCash() {
        return this;
    }
}
