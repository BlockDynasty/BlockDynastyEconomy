package lib.gui.templates.users.Cashmachine;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.IExtractItemUseCase;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.generics.CurrencySelectorAndAmount;

import java.math.BigDecimal;

public class ExtractorPanel extends CurrencySelectorAndAmount {
    private IExtractItemUseCase extractItemUseCase;

    public ExtractorPanel(IEntityGUI player, SearchCurrencyUseCase searchCurrencyUseCase, IGUI parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI, textInput);
        this.extractItemUseCase = HardCashUseCaseFactory.getExtractItemUseCase();
    }

    @Override
    public String execute(IEntityGUI sender, ICurrency currency, BigDecimal amount){
        extractItemUseCase.execute(sender.asEntityHardCash(),amount, currency.getSingular());
        return "";
    }
}
