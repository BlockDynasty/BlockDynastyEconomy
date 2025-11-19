package lib.gui.templates.administrators.subMenus.currencies;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.gui.GUIFactory;
import lib.gui.components.*;
import lib.gui.components.abstractions.PaginatedPanel;
import lib.util.colors.ChatColor;
import lib.util.colors.Message;
import lib.util.materials.Materials;

import java.util.Map;

public class CurrencyListExchange extends PaginatedPanel<Currency> {
    private final IEntityGUI player;
    private final EditCurrencyUseCase editCurrencyUseCase;
    private final Currency currency;

    public CurrencyListExchange(IEntityGUI player, EditCurrencyUseCase editCurrencyUseCase,IGUI abstractGUI,Currency currency) {
        super("Whitelist Exchange", 5, player, abstractGUI, 21);
        this.currency = currency;
        this.editCurrencyUseCase = editCurrencyUseCase;
        this.player = player;

        showItemsPage(currency.getInterchangeableWith());
    }

    @Override
    protected IItemStack createItemFor(Currency currency) {
        String color = ChatColor.stringValueOf(currency.getColor());
        return createItem(RecipeItem.builder().setMaterial(Materials.GOLD_INGOT)
                .setName(Message.process(Map.of("currency",color+currency.getSingular()),"CurrencySelector.button1.nameItem"))
                .setLore("Click to remove")
                .setTexture(currency.getTexture())
                .build());
    }

    @Override
    protected void functionLeftItemClick(Currency currency) {
        try {
            editCurrencyUseCase.removeInterchangeableCurrency(this.currency.getSingular(),currency.getSingular());
            player.sendMessage("Successfully removed exchange currency.");
            GUIFactory.currencyListExchange(player,this.currency,this.parent).open();
        }catch (Exception e){
            player.sendMessage("Error");
        }

    }

    @Override
    public void addCustomButtons(){
        setItem(39, createItem(Materials.NAME_TAG, "Add currency"), unused -> {
            GUIFactory.currencyListToAddExchange(player,currency,this).open();
        });
    }

}
