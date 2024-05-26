package com.combatreforged.metis.api.event.entity;

import com.combatreforged.metis.api.event.Event;
import com.combatreforged.metis.api.world.entity.Entity;

/**
 * Represents a {@link Entity} related event.
 */
public abstract class EntityEvent extends Event {
    private final Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }

    /**
     * Returns the {@link Entity} involved in this event.
     *
     * @return {@link Entity} involved in this event.
     */
    public Entity getEntity() {
        return entity;
    }
}
