package com.BlockDynasty.hytale.adapters;

import abstractions.platform.recipes.RecipeInventory;
import abstractions.platform.recipes.RecipeItem;
import au.ellie.hyui.builders.*;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
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
import domain.entity.currency.ItemStackCurrency;
import domain.entity.player.IEntityHardCash;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.GUISystem;
import lib.gui.components.ClickType;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IInventory;
import com.blockdynasty.economy.platform.IPlayer;

import java.util.List;
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
    public void removeItem(ItemStackCurrency itemStackCurrency) {
        //hardcash no implementar
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
        InventoryAdapter inventory = (InventoryAdapter) iInventory.getHandle();
        RecipeInventory recipeInventory = inventory.getRecipeInventory();


        PageBuilder pageBuilder = new PageBuilder(playerRef);
        GroupBuilder groupBuilder = new GroupBuilder();
        groupBuilder.withId("ParentGroup")
                .withAnchor(new HyUIAnchor().setWidth(800).setHeight(500))
                .withLayoutMode("TopScrolling")
                .inside("#Content")
                .addChild(LabelBuilder.label().withText(recipeInventory.getTitle()));

        ItemStackAdapter[] buttons = inventory.getItems();
        List<ItemStackAdapter> itemList =  java.util.Arrays.asList(buttons);
        itemList.forEach(item -> {
           RecipeItem recipeButton =  item.getRecipeItem();
           if(!recipeButton.getName().isEmpty()){
               String name =MessageAdapter.clearColorCodes(recipeButton.getName());
               String[] lore = recipeButton.getLore();

               ButtonBuilder buttonBuilder = ButtonBuilder.textButton();
               buttonBuilder
                       .withId("Button_" + name)
                       .withText(name)
                       .withTooltipTextSpan(MessageAdapter.formatVanillaMessage(lore))
                       .withStyle(new HyUIStyle()
                               .setAlignment("center")
                               .setFontSize(10)
                               .setTextColor("#FFFFFF")
                       )
                       .addEventListener(CustomUIEventBindingType.RightClicking, (ignored) -> {
                           //playerRef.sendMessage(Message.raw("Button " + name + " right-clicked!"));
                           GUISystem.handleClick(this, ClickType.RIGHT, itemList.indexOf(item));
                       })
                       .addEventListener(CustomUIEventBindingType.Activating, (ignored) -> {
                           GUISystem.handleClick(this, ClickType.LEFT, itemList.indexOf(item));
                          // playerRef.sendMessage(Message.raw("Button " + name + " clicked! slot:"+ itemList.indexOf(item)));
                       });
               groupBuilder.addChild(buttonBuilder);
           }

        });
        pageBuilder.addElement(groupBuilder);
        pageBuilder.open(playerRef.getReference().getStore());
        
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
