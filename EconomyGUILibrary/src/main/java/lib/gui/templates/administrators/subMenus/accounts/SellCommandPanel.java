package lib.gui.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.abstractions.PlatformAdapter;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.generics.CurrencySelectorAndAmount;
import java.math.BigDecimal;

public class SellCommandPanel extends CurrencySelectorAndAmount {
    private final IWithdrawUseCase withdrawUseCase;
    private final BlockDynasty.Economy.domain.entities.account.Player targetPlayer;
    private final PlatformAdapter platformAdapter;
    private final ITextInput textInput;

    public SellCommandPanel(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player targetPlayer,
                            SearchCurrencyUseCase searchCurrencyUseCase, IWithdrawUseCase withdrawUseCase, IGUI parentGUI, ITextInput textInput, PlatformAdapter platformAdapter) {
        super(player, searchCurrencyUseCase, parentGUI,textInput);
        this.platformAdapter = platformAdapter;
        this.targetPlayer = targetPlayer;
        this.withdrawUseCase = withdrawUseCase;
        this.textInput = textInput;
    }


    @Override
    public String execute(IEntityGUI sender, ICurrency currency, BigDecimal amount) {
        textInput.asInputChat().open(this,sender,"Command input","give "+targetPlayer.getNickname()+" <itemname> [amount]" ,
                s-> giveCommandToPlayer(sender,currency,amount,s)
        );
        return null;
    }

    private String giveCommandToPlayer(IEntityGUI sender, ICurrency currency,BigDecimal amount, String command){
        if(command.isEmpty()){
            return "Command cannot be empty.";
        }

        IEntityGUI playerEntity = platformAdapter.getPlayer(targetPlayer.getNickname());
        if(playerEntity == null){
            return "Player " + targetPlayer.getNickname() + " is not online. Cannot execute command.";
        }

        Result<Void> result = withdrawUseCase.execute(targetPlayer, currency.getSingular(), amount, Context.SYSTEM);
        if (!result.isSuccess()) {
            return "Failed to withdraw currency to player, reason: " + result.getErrorMessage();
        }

        try {
            platformAdapter.dispatchCommand(command);
        }catch (Exception e){
            return "Failed to execute command for player: " + targetPlayer.getNickname();
        }
        return "Command sold to player " + targetPlayer.getNickname()+" for a value of "+ amount +" "+ currency.getSingular()+".";
    }
}

