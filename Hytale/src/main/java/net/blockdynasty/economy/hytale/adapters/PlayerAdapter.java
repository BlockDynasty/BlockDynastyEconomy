package net.blockdynasty.economy.hytale.adapters;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import net.blockdynasty.economy.engine.platform.IPlayer;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IInventory;
import net.blockdynasty.economy.hardcash.domain.entity.currency.ItemStackCurrency;
import net.blockdynasty.economy.hardcash.domain.entity.player.IEntityHardCash;
import net.blockdynasty.economy.hytale.adapters.Gui.Hyui;

import java.util.UUID;

public class PlayerAdapter implements IPlayer {
    int notificationSound = SoundEvent.getAssetMap().getIndex("SFX_Drop_Items_Gems");
    int successSound = SoundEvent.getAssetMap().getIndex("SFX_Generic_Crafting_Failed");
    int failureSound = SoundEvent.getAssetMap().getIndex("SFX_Incorrect_Tool");
    private PlayerRef playerRef;
    private Ref<EntityStore> ref;
    private Store<EntityStore> store;

    public PlayerAdapter(PlayerRef playerRef) {
        this.playerRef = playerRef;
        this.ref= playerRef.getReference();
        if (ref!=null){
            this.store= ref.getStore();
        }

    }
    public PlayerAdapter(PlayerRef playerRef, Ref<EntityStore> ref, Store<EntityStore> store) {
        this.playerRef = playerRef;
        this.ref = ref;
        this.store = store;
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
        World world=Universe.get().getWorld(playerRef.getWorldUuid());
        world.execute(()->{
            TransformComponent transform = store.getComponent(ref, EntityModule.get().getTransformComponentType());
            SoundUtil.playSoundEvent3dToPlayer(ref, notificationSound, SoundCategory.UI, transform.getPosition(), store);
        });
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
    public int emptySlots() {
        //hardcash no implementar
        return 0;
    }

    @Override
    public void removeItem(ItemStackCurrency itemStackCurrency) {
        //hardcash no implementar
    }

    @Override
    public int takeAllItems(ItemStackCurrency itemStackCurrency) {
        //hardcash no implementar
        return 0;
    }

    @Override
    public boolean takeItems(ItemStackCurrency itemStackCurrency, int i) {
        //hardcash no implementar
        return false;
    }

    @Override
    public int countItems(ItemStackCurrency itemStackCurrency) {
        //hardcash no implementar
        return 0;
    }

    @Override
    public IEntityGUI asEntityGUI() {
        return this;
    }

    @Override
    public void playSuccessSound() {
      //  int index = SoundEvent.getAssetMap().getIndex("SFX_Cactus_Large_Hit");
        World world=Universe.get().getWorld(playerRef.getWorldUuid());
        world.execute(()->{
            TransformComponent transform = store.getComponent(ref, EntityModule.get().getTransformComponentType());
            SoundUtil.playSoundEvent3dToPlayer(ref, successSound, SoundCategory.UI, transform.getPosition(), store);
        });
    }

    @Override
    public void playFailureSound() {
        //int index = SoundEvent.getAssetMap().getIndex("SFX_Cactus_Large_Hit");
        World world=Universe.get().getWorld(playerRef.getWorldUuid());
        world.execute(()->{
            TransformComponent transform = store.getComponent(ref, EntityModule.get().getTransformComponentType());
            SoundUtil.playSoundEvent3dToPlayer(ref, failureSound, SoundCategory.UI, transform.getPosition(), store);
        });
    }

    //close GUI
    @Override
    public void closeInventory() {
        Player player = store.getComponent(ref, Player.getComponentType());
        player.getPageManager().setPage(ref, store, Page.None);
    }

    //open GUI la cual puede ser otra cosa que no sea un inventario
    @Override
    public void openInventory(IInventory iInventory) {
        Hyui.open(iInventory, playerRef, this);
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
