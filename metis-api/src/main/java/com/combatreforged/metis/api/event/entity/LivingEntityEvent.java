package com.combatreforged.metis.api.event.entity;

import com.combatreforged.metis.api.world.entity.LivingEntity;

/**
 * Represents a {@link LivingEntity} related event.
 */
public abstract class LivingEntityEvent extends EntityEvent {
    private final LivingEntity entity;

    public LivingEntityEvent(LivingEntity entity) {
        super(entity);
        this.entity = entity;
    }

    /**
     * Returns the {@link LivingEntity} involved in this event.
     *
     * @return {@link LivingEntity} involved in this event.
     */
    public LivingEntity getLivingEntity() {
        return entity;
    }
}
