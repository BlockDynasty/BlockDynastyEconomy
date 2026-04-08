package net.blockdynasty.economy.hytale.adapters.Gui;

import au.ellie.hyui.builders.HudBuilder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.blockdynasty.economy.gui.placeholder.IPlaceHolderDynastyEconomy;
import net.blockdynasty.economy.hytale.adapters.PlayerAdapter;
import net.blockdynasty.providers.services.ServiceProvider;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HudService {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final java.util.Map<PlayerRef, au.ellie.hyui.builders.HyUIHud> playerHuds = new java.util.concurrent.ConcurrentHashMap<>();
    private static IPlaceHolderDynastyEconomy placeHolderDynastyEconomy;

    public static void start() {
        Optional<IPlaceHolderDynastyEconomy> optional= ServiceProvider.get(IPlaceHolderDynastyEconomy.class);
        placeHolderDynastyEconomy = optional.orElse(null);

        scheduler.scheduleAtFixedRate(() -> {
            playerHuds.forEach(HudService::update);
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public static void addHud(PlayerRef playerRef, au.ellie.hyui.builders.HyUIHud hud) {
        playerHuds.put(playerRef, hud);
    }

    public static void removeHud(PlayerRef playerRef) {
        playerHuds.remove(playerRef);
    }

    private static void update(PlayerRef playerRef, au.ellie.hyui.builders.HyUIHud hud) {
            Ref<EntityStore> ref = playerRef.getReference();
            if (ref == null || !ref.isValid()) return;

            Store<EntityStore> store = ref.getStore();
            World world = store.getExternalData().getWorld();
            world.execute(() -> {
                if (!ref.isValid()) {
                    return; // The scheduler will continue but do nothing, optionally cancel the future
                }
                hud.update(HudBuilder.hudForPlayer(playerRef)
                        .fromHtml("""
                                   <div style="layout-mode: top">
                                     <button id="TextButton" style="color: #ffffff; anchor-left: 0; anchor-right: 1000; anchor-top: 1000; anchor-bottom: 0; anchor-width: 5; anchor-height: 5">%s</button>
                                   </div>
                                   """.formatted(placeHolderDynastyEconomy.onRequest(new PlayerAdapter(playerRef), "balance_default_formatted"))));
            });
    }
}
