package com.combatreforged.metis.api.event.entity;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.damage.DamageData;
import com.combatreforged.metis.api.world.entity.LivingEntity;

/**
 * Called when an entity is damaged.
 */
public class LivingEntityDamageEvent extends LivingEntityEvent implements Cancellable {
    public static final EventBackend<LivingEntityDamageEvent> BACKEND = EventBackend.create(LivingEntityDamageEvent.class);

    private boolean cancelled;

    private DamageData cause;
    private float damage;

    public LivingEntityDamageEvent(LivingEntity entity, DamageData cause, float damage) {
        super(entity);
        this.cause = cause;
        this.damage = damage;

        this.cancelled = false;
    }

    /**
     * Gets the Cause for this event.
     *
     * @return Cause for this event.
     */
    public DamageData getCause() {
        return cause;
    }

    /**
     * Sets the Cause for this event.
     *
     * @param cause Cause for this event.
     */
    public void setCause(DamageData cause) {
        this.cause = cause;
    }

    /**
     * Gets the amount of damage the entity gets.
     *
     * @return the amount of damage the entity gets
     */
    public float getDamage() {
        return damage;
    }

    /**
     * Sets the amount of damage the entity should gets.
     *
     * @param damage the amount of damage the entity should get
     */
    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
