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

package platform.listeners;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.entities.offers.Offer;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.events.offersEvents.OfferAccepted;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCanceled;
import BlockDynasty.Economy.domain.events.offersEvents.OfferCreated;
import BlockDynasty.Economy.domain.events.offersEvents.OfferExpired;
import BlockDynasty.Economy.domain.events.transactionsEvents.*;
import lib.abstractions.PlatformAdapter;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.GUIFactory;
import lib.gui.GUISystem;
import lib.scheduler.ContextualTask;
import lib.util.colors.ChatColor;
import services.Message;

import java.math.BigDecimal;
import java.util.Map;

public class EventListener {

    public static void register(EventManager eventManager, PlatformAdapter platformAdapter) {

        eventManager.subscribe(PayEvent.class, event -> {
            IEntityCommands player = platformAdapter.getPlayer(event.getPayer().getNickname());
            IEntityCommands target = platformAdapter.getPlayer(event.getReceived().getNickname());

            ICurrency currency = event.getCurrency();
            String format = currency.format(event.getAmount());
            String receiverName = event.getReceived().getNickname();
            String senderName = event.getPayer().getNickname();

            if (player != null){
                player.sendMessage(Message.process(Map.of("currency",ChatColor.stringValueOf(currency.getColor())+format, "playerName",receiverName),"pay1"));
                player.playNotificationSound();

                Runnable task=()->{
                    GUISystem.refresh(player.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , player));
            }

            if (target != null){
                target.sendMessage(Message.process(Map.of("currency",ChatColor.stringValueOf(currency.getColor())+format,"playerName",senderName),"pay2"));
                target.playNotificationSound();

                Runnable task=()->{
                    GUISystem.refresh(target.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , target));
            }
        });

        eventManager.subscribe(TransferEvent.class, event -> {
            IEntityCommands player = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            IEntityCommands target = platformAdapter.getPlayer(event.getToPlayer().getNickname());

            ICurrency currency = event.getCurrency();
            String format = currency.format(event.getAmount());
            String receiverName = event.getToPlayer().getNickname();
            String senderName = event.getFromPlayer().getNickname();


            if (player != null) {
                player.sendMessage(Message.process(Map.of("currency",ChatColor.stringValueOf(currency.getColor())+format, "playerName",receiverName),"transfer1"));
                player.playNotificationSound();

                Runnable task=()->{
                    GUISystem.refresh(player.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , player));
            }

            if (target != null) {
                target.sendMessage(Message.process(Map.of("currency",ChatColor.stringValueOf(currency.getColor())+format, "playerName",senderName),"transfer2"));
                target.playNotificationSound();

                Runnable task=()->{
                    GUISystem.refresh(target.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , target));
            }
        });

        eventManager.subscribe(ExchangeEvent.class, event -> {
            IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());

            ICurrency fromCurrency = event.getFromCurrency();
            String fromFormat = fromCurrency.format(event.getAmount());

            ICurrency toCurrency = event.getToCurrency();
            String toFormat = toCurrency.format(event.getExchangedAmount());

            if (player != null) {
                player.sendMessage(Message.process(Map.of("fromCurrency",ChatColor.stringValueOf(fromCurrency.getColor())+fromFormat,"toCurrency",ChatColor.stringValueOf(toCurrency.getColor())+toFormat),"exchange"));
                player.playNotificationSound();
            }
        });

        eventManager.subscribe(TradeEvent.class, event -> {
            IEntityCommands sender = platformAdapter.getPlayer(event.getFromPlayer().getNickname());
            IEntityCommands receiver = platformAdapter.getPlayer(event.getToPlayer().getNickname());

            ICurrency fromCurrency = event.getCurrencyFrom();
            String fromFormat = fromCurrency.format(event.getAmountFrom());

            ICurrency toCurrency = event.getCurrencyTo();
            String toFormat = toCurrency.format(event.getAmountTo());

            if (sender != null ) {
                sender.sendMessage(Message.process(
                        Map.of("fromCurrency",ChatColor.stringValueOf(fromCurrency.getColor())+fromFormat,
                                "toCurrency",ChatColor.stringValueOf(toCurrency.getColor())+toFormat,
                                "playerName",event.getToPlayer().getNickname()
                        ),"trade1"));
                sender.playNotificationSound();

                Runnable task=()->{
                    GUISystem.refresh(sender.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));

            }
            if (receiver != null){
                receiver.sendMessage(Message.process(
                        Map.of("fromCurrency",ChatColor.stringValueOf(fromCurrency.getColor())+fromFormat,
                                "toCurrency",ChatColor.stringValueOf(toCurrency.getColor())+toFormat,
                                "playerName",event.getFromPlayer().getNickname()
                ),"trade2"));
                receiver.playNotificationSound();

                Runnable task=()->{
                    GUISystem.refresh(receiver.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

        });

        eventManager.subscribe(DepositEvent.class, event -> {
            if (event.getContext() == Context.COMMAND){
                IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage(Message.process(Map.of(
                            "currency",ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount())
                            ),"deposit"));
                    player.playNotificationSound();

                    Runnable task=()->{
                        GUISystem.refresh(player.getUniqueId());
                    };
                    platformAdapter.getScheduler().run(ContextualTask.build(task , player));
                }
            }

        });

        eventManager.subscribe(WithdrawEvent.class, event -> {
            if (event.getContext() == Context.COMMAND){
                IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage(Message.process(Map.of(
                            "currency",ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount())
                    ),"withdraw"));
                    player.playNotificationSound();

                    Runnable task=()->{
                        GUISystem.refresh(player.getUniqueId());
                    };
                    platformAdapter.getScheduler().run(ContextualTask.build(task , player));
                }
            }
        });

        eventManager.subscribe(SetEvent.class, event -> {
            if (event.getContext() == Context.COMMAND){
                IEntityCommands player = platformAdapter.getPlayer(event.getPlayer().getNickname());
                if (player != null) {
                    player.sendMessage(Message.process(Map.of(
                            "currency",ChatColor.stringValueOf(event.getCurrency().getColor()) +event.getCurrency().format(event.getAmount())
                    ),"set"));
                    player.playNotificationSound();

                    Runnable task=()->{
                        GUISystem.refresh(player.getUniqueId());
                    };
                    platformAdapter.getScheduler().run(ContextualTask.build(task , player));
                }
            }
        });

        eventManager.subscribe(OfferCreated.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());

            ICurrency currencyOffered = offer.getTipoCantidad();
            BigDecimal amountOffered = offer.getCantidad();
            ICurrency currencyValue = offer.getTipoMonto();
            BigDecimal amountValue = offer.getMonto();

            String fromFormat = currencyOffered.format(amountOffered);
            String toFormat = currencyValue.format(amountValue);

            if (sender != null) {
                sender.sendMessage(Message.process(
                        Map.of("playerName", offer.getComprador().getNickname(),
                                "fromCurrency",ChatColor.stringValueOf(currencyOffered.getColor()) + fromFormat,
                                "toCurrency",ChatColor.stringValueOf(currencyValue.getColor()) + toFormat
                ),"offerCreated1"));
            }
            if (receiver != null){
                receiver.sendMessage(Message.process(
                        Map.of("playerName", offer.getVendedor().getNickname(),
                                "fromCurrency",ChatColor.stringValueOf(currencyOffered.getColor())+ fromFormat,
                                "toCurrency",ChatColor.stringValueOf(currencyValue.getColor()) + toFormat
                        ),"offerCreated2"));
                receiver.playNotificationSound();

                Runnable task1=()->{
                    GUISystem.refresh(offer.getComprador().getUuid());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task1 , receiver));
            }
        });

        eventManager.subscribe(OfferCanceled.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());

            if (receiver != null ) {
                receiver.sendMessage(Message.process(Map.of("playerName",offer.getVendedor().getNickname()),"offerCanceled1"));

                Runnable task = () -> {
                    GUISystem.refresh(receiver.getUniqueId());
                };

                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

            if (sender != null){
                sender.sendMessage(Message.process(Map.of("playerName",offer.getComprador().getNickname()),"offerCanceled2"));

                Runnable task = () -> {
                    GUISystem.refresh(sender.getUniqueId());
                };

                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));
            }
        });

        eventManager.subscribe(OfferExpired.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());

            if (receiver != null ) {
                receiver.sendMessage(Message.process(Map.of("playerName", offer.getVendedor().getNickname()),"offerExpired1"));
                Runnable task = () -> {
                    GUISystem.refresh(receiver.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

            if (sender != null){
                sender.sendMessage(Message.process(Map.of("playerName", offer.getComprador().getNickname()),"offerExpired2"));
                Runnable task = () -> {
                    GUISystem.refresh(sender.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));
            }
        });

        eventManager.subscribe(OfferAccepted.class, event -> {
            Offer offer = event.getOffer();
            IEntityCommands receiver = platformAdapter.getPlayerByUUID(offer.getComprador().getUuid());
            IEntityCommands sender = platformAdapter.getPlayerByUUID(offer.getVendedor().getUuid());

            if (receiver != null ) {
                Runnable task=()->{
                    GUISystem.refresh(receiver.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , receiver));
            }

            if (sender != null){
                Runnable task=()->{
                    GUISystem.refresh(sender.getUniqueId());
                };
                platformAdapter.getScheduler().run(ContextualTask.build(task , sender));
            }
        });
    }
}
