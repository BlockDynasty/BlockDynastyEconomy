package net.blockdynasty.economy.hytale.adapters.listener;

import au.ellie.hyui.builders.*;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.blockdynasty.economy.hytale.adapters.Gui.HudService;


public class PlayerReadyListener {


    public void onPlayerReady(PlayerReadyEvent event){
        var player = event.getPlayer();
        if (player == null) return;

        Ref<EntityStore> ref = player.getReference();
        if (ref == null || !ref.isValid()) return;

        Store<EntityStore> store = ref.getStore();
        World world = store.getExternalData().getWorld();

        world.execute(() -> {
            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef == null) return;
            HyUIHud hud= HudBuilder.hudForPlayer(playerRef).show();
            HudService.addHud(playerRef, hud);
        });
    }
}