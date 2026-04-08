package net.blockdynasty.economy.hytale.adapters.Gui;

import au.ellie.hyui.builders.*;
import au.ellie.hyui.types.DefaultStyles;
import au.ellie.hyui.types.SliderStyle;
import au.ellie.hyui.types.TextButtonStyle;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.blockdynasty.economy.engine.platform.IPlayer;
import net.blockdynasty.economy.gui.gui.GUISystem;
import net.blockdynasty.economy.gui.gui.components.ClickType;
import net.blockdynasty.economy.gui.gui.components.IInventory;
import net.blockdynasty.economy.hytale.adapters.InventoryAdapter;
import net.blockdynasty.economy.hytale.adapters.ItemStackAdapter;
import net.blockdynasty.economy.hytale.adapters.MessageAdapter;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeInventory;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;

import java.util.List;

public class Hyui {
    public static void open(IInventory iInventory, PlayerRef playerRef, IPlayer player){
        InventoryAdapter inventory = (InventoryAdapter) iInventory.getHandle();
        RecipeInventory recipeInventory = inventory.getRecipeInventory();

        GroupBuilder groupBuilder = GroupBuilder.group()
                .withId("ParentGroup")
                .withAnchor(new HyUIAnchor().setTop(20))
                .withLayoutMode("TopScrolling")
                .inside("#Content");
                //.addChild(LabelBuilder.label().withText(recipeInventory.getTitle()));

        ItemStackAdapter[] buttons = inventory.getItems();
        List<ItemStackAdapter> itemList =  java.util.Arrays.asList(buttons);
        itemList.forEach(item -> {
            RecipeItem recipeButton =  item.getRecipeItem();
            if(!recipeButton.getName().isEmpty()){
                String name = MessageAdapter.clearColorCodes(recipeButton.getName());
                String[] lore = recipeButton.getLore();

                ButtonBuilder buttonBuilder = ButtonBuilder.textButton()
                        .withId("Button_" + name)
                        .withText(name)
                        .withTooltipTextSpan(MessageAdapter.formatVanillaMessage(lore))
                        .withStyle(TextButtonStyle.primaryStyle())
                        .addEventListener(CustomUIEventBindingType.RightClicking, (ignored) -> {
                            GUISystem.handleClick(player, ClickType.RIGHT, itemList.indexOf(item));
                        })
                        .addEventListener(CustomUIEventBindingType.Activating, (ignored) -> {
                            GUISystem.handleClick(player, ClickType.LEFT, itemList.indexOf(item));
                        });
                groupBuilder.addChild(buttonBuilder);
            }

        });
        PageBuilder.pageForPlayer(playerRef)
                .addElement(PageOverlayBuilder.pageOverlay()
                        .addChild(ContainerBuilder.decoratedContainer()
                                .withAnchor(new HyUIAnchor()
                                        //.setTop(20)
                                        //.setRight(30)
                                        //.setLeft(30)
                                        //.setBottom(10)
                                        .setHeight(450)
                                        .setWidth(500)
                                )
                                .withTitleText(recipeInventory.getTitle())
                                .addContentChild(groupBuilder))
                )
                .open(playerRef.getReference().getStore());

    }
}
