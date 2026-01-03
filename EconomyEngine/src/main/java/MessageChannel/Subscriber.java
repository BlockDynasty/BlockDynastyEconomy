package MessageChannel;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.services.courier.PlayerTargetMessage;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;
import lib.gui.GUISystem;
import lib.scheduler.ContextualTask;

import java.util.UUID;

public abstract class Subscriber {
    private final PlatformAdapter platformAdapter;
    private final IOfferService offerService;
    private final EventManager eventManager;
    private final ICurrencyService currencyService;
    private final IAccountService accountService;

    public Subscriber(PlatformAdapter platformAdapter,
                      IOfferService offerService, ICurrencyService currencyService,
                      IAccountService accountService, EventManager eventManager) {
        this.platformAdapter = platformAdapter;
        this.offerService = offerService;
        this.eventManager = eventManager;
        this.currencyService = currencyService;
        this.accountService = accountService;
    }

    public void processMessage(String messageString) {
        PlayerTargetMessage message = PlayerTargetMessage.builder()
                .fromJson(messageString)
                .build();
        if(message.isSameOrigin()){
            return;
        }
        switch (message.getType()) {
            case EVENT:
                offerService.processNetworkEvent(message.getData());
                if(shouldSkipProcessing(message.getTarget())){
                    break;
                }
                if(message.getTargetPlayer() != null){
                    platformAdapter.getScheduler().runAsync(
                            ContextualTask.build(
                                    () -> {
                                        accountService.syncOnlineAccount(message.getTargetPlayer());
                                        //GUISystem.refresh(message.getTarget());
                                        eventManager.processNetworkEvent(message.getData());
                                    }
                            )
                    );
                    break;
                }
                eventManager.processNetworkEvent(message.getData());
                break;
            case ACCOUNT:
                if(shouldSkipProcessing(message.getTarget())){
                    break;
                }
                if(message.getTargetPlayer() != null){
                    platformAdapter.getScheduler().runAsync(
                            ContextualTask.build(
                                    () -> {
                                        accountService.syncOnlineAccount(message.getTargetPlayer());
                                        GUISystem.refresh(message.getTarget());
                                    }
                            )
                    );
                }
                break;
            case CURRENCY:
                currencyService.syncCurrency(message.getTarget());
                break;
        }
    }

    private boolean shouldSkipProcessing(UUID target) {
        IPlayer player = platformAdapter.getPlayerByUUID(target);
        return player == null || !player.isOnline();
    }
}