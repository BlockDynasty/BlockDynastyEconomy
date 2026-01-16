package lib.gui.templates.users.Cashmachine;


import abstractions.platform.materials.Materials;
import abstractions.platform.recipes.RecipeItem;
import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.IDepositItemUseCase;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.factory.Item;
import lib.gui.components.generics.AbstractPanel;
import lib.gui.components.generics.Button;
import services.messages.Message;
import util.colors.ChatColor;
import util.colors.Colors;

import java.util.Map;

//en ella se va a ver un boton para extraer o para depositar
public class MachinePanel extends AbstractPanel {
    private IDepositItemUseCase depositItemUseCase;

    public MachinePanel(IEntityGUI owner, IGUI parent) {
        super(Message.process("MachinePanel.title"), 3, owner, parent);
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositItemUseCase();
        renderView();
    }

    public void renderView(){
        setButton(12,
                Button.builder()
                        .setItemStack(Item.of(RecipeItem.builder()
                                        .setMaterial(Materials.EMERALD)
                                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"MachinePanel.button1.nameItem"))
                                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"MachinePanel.button1.lore"))
                                .build()))
                        .setLeftClickAction(event -> {
                            if(event.hasPermission("BlockDynastyEconomy.players.depositCash")){
                                this.depositItemUseCase.execute(owner.asEntityHardCash());
                            }else {
                                event.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                            }
                        })
                .build()
        );

        setButton(14, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setName(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.GOLD)),"MachinePanel.button2.nameItem"))
                        .setLore(Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"MachinePanel.button2.lore"))
                                .setMaterial(Materials.DIAMOND)
                        .build()))
                .setLeftClickAction(event -> {
                    if(event.hasPermission("BlockDynastyEconomy.players.extractCash")){
                        GUIFactory.extractorPanel(event,this).open();
                    }else {
                        event.sendMessage(ChatColor.stringValueOf(Colors.RED)+"You don't have permission");
                    }
                })
                .build());



        setButton(22, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.BARRIER)
                        .setName( Message.process(Map.of("color", ChatColor.stringValueOf(Colors.RED)),"Paginated.button4.nameItem"))
                        .setLore( Message.process(Map.of("color", ChatColor.stringValueOf(Colors.WHITE)),"Paginated.button4.lore"))
                        .build())
                )
                .setLeftClickAction(unused -> this.openParent())
                .build());

    }

}
