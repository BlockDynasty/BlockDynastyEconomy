/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lib.gui.templates.users.Offers;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.ITextInput;
import lib.gui.components.Materials;
import lib.gui.components.abstractions.CurrencySelectorAndAmount;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

public class CreateOfferSecondPanel extends CurrencySelectorAndAmount {
    private final BlockDynasty.Economy.domain.entities.account.Player target;
    private final BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase createOfferUseCase;
    private final CreateOfferFirstPanel parentGUI;

    public CreateOfferSecondPanel(IEntityGUI player, BlockDynasty.Economy.domain.entities.account.Player target, SearchCurrencyUseCase searchCurrencyUseCase,
                                  CreateOfferUseCase createOfferUseCase, CreateOfferFirstPanel parentGUI, ITextInput textInput) {
        super(player, searchCurrencyUseCase, parentGUI,parentGUI.getCurrency(), textInput);
        this.createOfferUseCase = createOfferUseCase;
        this.target = target;
        this.parentGUI = parentGUI;
    }

    @Override
    protected String  execute(IEntityGUI sender, Currency currency, java.math.BigDecimal amount){
        Result<Void> result= createOfferUseCase.execute(
                sender.getUniqueId(),
                target.getUuid(),
                parentGUI.getCurrency().getPlural(),
                parentGUI.getAmount(),
                currency.getSingular(),
                amount
        );

        if (result.isSuccess()){
            GUIFactory.myActiveOffers(sender).open();

        }else{
            sender.sendMessage(ChatColor.stringValueOf(Colors.RED)+"Error: " + result.getErrorMessage()+ "."+result.getErrorCode());
        }

        return null;
    }

    @Override
    public void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, ChatColor.stringValueOf(Colors.GREEN)+"Select Currency to Receive",
                        ChatColor.stringValueOf(Colors.WHITE)+"Click to select the currency you want to receive",ChatColor.stringValueOf(Colors.WHITE)+ "And before that, the amount"
                ),
                null);

    }
}
