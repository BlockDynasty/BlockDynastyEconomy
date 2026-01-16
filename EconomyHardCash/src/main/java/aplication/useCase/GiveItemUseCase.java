package aplication.useCase;

import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.result.Result;
import domain.entity.currency.ItemStackCurrency;
import domain.entity.platform.HardCashCreator;
import domain.entity.player.IEntityHardCash;
import domain.service.ItemCreator;
import domain.service.ItemCreatorFactory;

import java.math.BigDecimal;

public class GiveItemUseCase implements IGiveItemUseCase{
    private SearchCurrencyUseCase searchCurrencyUseCase;
    private HardCashCreator hardCashCreator;
    private ItemCreator itemCreator;

    public GiveItemUseCase(SearchCurrencyUseCase searchCurrencyUseCase, HardCashCreator platform) {
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.hardCashCreator= platform;
        this.itemCreator = ItemCreatorFactory.getItemCreator(platform);
    }

    @Override
    public boolean execute(String playerName, BigDecimal amount, String currency) {
        Result<ICurrency> currencyResult = searchCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            return false;
        }
        ICurrency currencyData = currencyResult.getValue();
        ItemStackCurrency item = itemCreator.create(currencyData, amount);

        IEntityHardCash player =hardCashCreator.getPlayer(playerName);

        if (player != null) {
            if (player.hasItem(item) || player.hasEmptySlot()){
                player.giveItem(item);
                player.sendMessage("You have received " + amount + " " + currency + " in items.");
                return true;
            }
        }
        return false;
    }
}
