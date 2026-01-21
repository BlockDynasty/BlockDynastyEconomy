package com.BlockDynasty.hytale.adapters.sheduler;

import abstractions.platform.scheduler.ILocation;
import com.hypixel.hytale.server.core.universe.world.World;

public class LocationAdapter implements ILocation {
    private World world;

    public LocationAdapter(World world) {
        this.world = world;
    }


    @Override
    public Object getRoot() {
        return world;
    }
}
