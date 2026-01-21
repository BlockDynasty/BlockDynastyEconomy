package com.BlockDynasty.hytale.adapters.sheduler;

import abstractions.platform.scheduler.IEntity;
import com.hypixel.hytale.server.core.entity.Entity;

public class EntityAdapter implements IEntity {
    private Entity entity;
    public EntityAdapter(Entity entity) {
        this.entity = entity;
    }
    @Override
    public Object getRoot() {
        return entity;
    }
}
