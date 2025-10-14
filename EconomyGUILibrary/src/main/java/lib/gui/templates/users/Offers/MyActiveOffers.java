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

import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.aplication.useCase.offer.SearchOfferUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.components.IGUI;
import lib.gui.components.IItemStack;
import lib.gui.components.IEntityGUI;
import lib.gui.components.Materials;
import lib.gui.components.abstractions.PaginatedPanel;
import lib.gui.GUIFactory;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;
import lib.util.colors.Message;

import java.util.List;
import java.util.Map;

public class MyActiveOffers extends PaginatedPanel<Offer> {
    private final CancelOfferUseCase cancelOfferUseCase;
    private final SearchOfferUseCase searchOfferUseCase;
    private final IEntityGUI player;

    public MyActiveOffers(SearchOfferUseCase searchOfferUseCase,
                          CancelOfferUseCase cancelOfferUseCase,
                          IEntityGUI player, IGUI parent) {
        super("My Active Offers", 5, player, parent, 21);
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.searchOfferUseCase = searchOfferUseCase;
        this.player = player;

        List<Offer> offers = searchOfferUseCase.getOffersSeller(player.getUniqueId());
        showItemsPage(offers);
    }

    @Override
    public void refresh(){
        GUIFactory.myActiveOffers(player).open();
    }

    @Override
    public void openParent() {
        GUIFactory.bankPanel(player).open();
    }

    @Override
    protected IItemStack createItemFor(Offer offer) {
        Player comprador = offer.getComprador();

        Currency tipoCantidad = offer.getTipoCantidad();
        Currency tipoMonto = offer.getTipoMonto();

        return createItem(
                Materials.PLAYER_HEAD,
                Message.process(Map.of("playerName", comprador.getNickname()), "MyActiveOffers.button1.nameItem"),
                Message.processLines(Map.of(
                        "amount", ChatColor.stringValueOf(tipoCantidad.getColor()) + tipoCantidad.format(offer.getCantidad()),
                        "price", ChatColor.stringValueOf(tipoMonto.getColor()) + tipoMonto.format(offer.getMonto()),
                        "color1", ChatColor.stringValueOf(Colors.GREEN)), "MyActiveOffers.button1.lore")
            );
    }
    @Override
    protected void functionLeftItemClick(Offer offer) {
        Result<Void> result =cancelOfferUseCase.execute(offer.getVendedor());
        if (result.isSuccess()) {
            GUIFactory.myActiveOffers(player).open();
            player.sendMessage("Offer cancelled");
        } else {
            this.close();
            player.sendMessage("Failed to cancel offer: " + result.getErrorMessage());
        }
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Materials.PAPER, Message.process("MyActiveOffers.button2.nameItem"),
                        Message.process("MyActiveOffers.button2.lore")),
                unused -> {
                    refresh();
                });
    }
}