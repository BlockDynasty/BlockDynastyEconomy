package EngineTest.mocks;

import EngineTest.mocks.utils.Color;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.player.IEntityHardCash;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IInventory;
import platform.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player implements IPlayer {
    private List<String> permissions;
    private UUID uuid;
    private String name;
    private boolean op = false;

    public Player(UUID uuid, String name) {
        this.permissions = new ArrayList<>();
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void playNotificationSound() {

    }

    @Override
    public boolean isOnline() {
       return MinecraftServer.isOnline(this);
    }

    @Override
    public boolean hasPermission(String permission) {
        if(op){
            return true;
        }else {
            return this.permissions.contains(permission);
        }
    }

    @Override
    public void kickPlayer(String message) {

    }

    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(this.name+" "+"received message: "+ Color.parse(message));
    }

    @Override
    public Object getRoot() {
        return this;
    }

    @Override
    public void playSuccessSound() {

    }

    @Override
    public void playFailureSound() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public void openInventory(IInventory inventory) {
        //System.out.println("Inventory "+inventory.getTitle()+" opened for player "+this.getName());
        System.out.println(inventory);
    }

    @Override
    public IEntityCommands asEntityCommands() {
        return this;
    }

    @Override
    public IEntityHardCash asEntityHardCash() {
        return null;
    }

    public void addPermission(String permission){
        this.permissions.add(permission);
    }
    public void removePermission(String permission){this.permissions.remove(permission);}

    public void setOp(boolean op){
        this.op = op;
    }

    @Override
    public void giveItem(ItemStackCurrency item) {

    }

    @Override
    public ItemStackCurrency takeHandItem() {
        return null;
    }

    @Override
    public boolean hasItem(ItemStackCurrency itemCurrency) {
        return false;
    }

    @Override
    public boolean hasEmptySlot() {
        return false;
    }

    @Override
    public void removeItem(ItemStackCurrency itemCurrency) {

    }
}
