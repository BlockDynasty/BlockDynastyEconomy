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

import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.GUIFactory;
import lib.gui.components.IGUI;
import lib.gui.components.IItemStack;
import lib.gui.components.IEntityGUI;
import lib.gui.components.factory.Item;
import lib.gui.components.generics.Button;
import abstractions.platform.recipes.RecipeItem;
import abstractions.platform.materials.Materials;
import lib.gui.components.generics.PaginatedPanel;
import util.colors.ChatColor;
import util.colors.Colors;
import services.messages.Message;

import java.util.List;
import java.util.Map;

public class ReceivedOffers extends PaginatedPanel<Offer> {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final IEntityGUI sender;
    public ReceivedOffers(SearchOfferUseCase searchOfferUseCase, AcceptOfferUseCase acceptOfferUseCase,
                          CancelOfferUseCase cancelOfferUseCase,
                          IEntityGUI sender, IGUI parent) {
        super(Message.process("ReceivedOffers.title"), 5, sender, parent, 21);
        this.acceptOfferUseCase = acceptOfferUseCase;
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.searchOfferUseCase = searchOfferUseCase;
        this.sender = sender;

        //can be Player
        List<Offer> offers = searchOfferUseCase.getOffersBuyer(sender.getUniqueId());
        showItemsPage(offers);
    }

    @Override
    public void refresh(){
        //List<Offer> offers = searchOfferUseCase.getOffersBuyer(sender.getUniqueId());
        //showItemsPage(offers);
        GUIFactory.receivedOffers(sender,getParent()).open();
    }

    @Override
    protected IItemStack createItemFor(Offer offer) {
        Player vendedor = offer.getVendedor();

        ICurrency tipoCantidad = offer.getTipoCantidad();
        ICurrency tipoMonto = offer.getTipoMonto();

        return Item.of(
                RecipeItem.builder()
                        .setMaterial(Materials.PLAYER_HEAD)
                        .setName(Message.process(Map.of("playerName", vendedor.getNickname()), "ReceivedOffers.button1.nameItem"))
                        .setLore( Message.processLines(Map.of(
                                "amount", ChatColor.stringValueOf(tipoCantidad.getColor()) + tipoCantidad.format(offer.getCantidad()),
                                "price", ChatColor.stringValueOf(tipoMonto.getColor()) + tipoMonto.format(offer.getMonto()),
                                "color1", ChatColor.stringValueOf(Colors.GREEN),
                                "color2", ChatColor.stringValueOf(Colors.RED)), "ReceivedOffers.button1.lore"))
                        .build()
            );

    }

    @Override
    protected void functionLeftItemClick(Offer offer) {
        //can be player
        Result<Void> result =acceptOfferUseCase.execute(offer.getComprador().getUuid(),offer.getVendedor().getUuid());
        if (result.isSuccess()) {
            sender.sendMessage("Offer accepted successfully!");
        } else {
            sender.sendMessage("Failed to accept offer: " + result.getErrorMessage());
        }
        GUIFactory.receivedOffers(sender,getParent()).open();
    }

    @Override
    protected void functionRightItemClick(Offer offer) {
        Result<Void> result =cancelOfferUseCase.execute(offer.getVendedor());
        if (result.isSuccess()) {
            sender.sendMessage("Offer cancelled");
        } else {
            sender.sendMessage("Failed to cancel offer: " + result.getErrorMessage());
        }
        GUIFactory.receivedOffers(sender,getParent()).open();
    }

    @Override
    protected void addCustomButtons() {

        setButton(4, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setMaterial(Materials.PAPER)
                        .setName(Message.process("ReceivedOffers.button2.nameItem"))
                        .setLore( Message.processLines("ReceivedOffers.button2.lore"))
                        .build()))
                .setLeftClickAction(unused -> {refresh();})
                .build());
    }

}