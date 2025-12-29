package EngineTest.mocks;

import EngineTest.mocks.utils.Color;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lib.abstractions.IConsole;
import lib.abstractions.IPlayer;
import lib.abstractions.IProxySubscriber;
import lib.abstractions.PlatformAdapter;
import lib.gui.components.recipes.RecipeItem;
import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import lib.gui.components.ITextInput;
import lib.gui.components.recipes.RecipeInventory;
import lib.scheduler.IScheduler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Platform implements PlatformAdapter {

    @Override
    public IPlayer getPlayer(String name) {
        return MinecraftServer.getOnlinePlayers().stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        return MinecraftServer.getOnlinePlayers().stream().filter(p -> p.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        return new ArrayList<>(MinecraftServer.getOnlinePlayers());
    }

    @Override
    public IItemStack createItemStack(RecipeItem recipeItem) {
        return new ItemStack(recipeItem);
    }

    @Override
    public IInventory createInventory(RecipeInventory recipeInventory) {
        return new Inventory(recipeInventory);
    }

    @Override
    public void dispatchCommand(String command) throws Exception {
        System.out.println("[BlockDynastyEconomy] "+command);
    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) {
        System.out.println("Plugin message sent on channel: "+channel);
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String jsonMessage = in.readUTF();
            JsonElement outer = JsonParser.parseString(jsonMessage);
            System.out.println("Message: "+outer);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void registerMessageChannel(IProxySubscriber subscriber) {

    }

    @Override
    public IScheduler getScheduler() {
        return new Scheduler();
    }

    @Override
    public IConsole getConsole() {
        return new Console();
    }

    @Override
    public File getDataFolder() {
        return new File("./src/test/java/EngineTest/data");
    }

    @Override
    public boolean isLegacy() {
        return false;
    }

    @Override
    public boolean isOnlineMode() {
        return true;
    }

    @Override
    public boolean hasSupportAdventureText() {
        boolean hasAdventure= true;
        Color.init(hasAdventure);
        return hasAdventure;
    }

    @Override
    public ITextInput getTextInput() {
        return new TextInput();
    }
}
