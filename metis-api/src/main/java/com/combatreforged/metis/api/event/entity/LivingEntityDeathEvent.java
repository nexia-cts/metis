package com.combatreforged.metis.api.event.entity;

import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.damage.DamageData;
import com.combatreforged.metis.api.world.entity.LivingEntity;

/**
 * Throws whenever a {@link LivingEntity} dies.
 */
public class LivingEntityDeathEvent extends LivingEntityEvent {
    public static final EventBackend<LivingEntityDeathEvent> BACKEND = EventBackend.create(LivingEntityDeathEvent.class);

    private DamageData cause;
    private boolean dropLoot;
    private boolean dropEquipment;
    private boolean dropExperience;

    public LivingEntityDeathEvent(LivingEntity entity, DamageData cause, boolean dropLoot, boolean dropEquipment, boolean dropExperience) {
        super(entity);
        this.cause = cause;
        this.dropLoot = dropLoot;
        this.dropEquipment = dropEquipment;
        this.dropExperience = dropExperience;
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
     * Gets if the loot of the player drops.
     *
     * @return True if the loot of the player drops.
     */
    public boolean isDropLoot() {
        return dropLoot;
    }

    /**
     * Gets if the loot of the player drops.
     *
     * @param dropLoot True if the loot of the player should drop.
     */
    public void setDropLoot(boolean dropLoot) {
        this.dropLoot = dropLoot;
    }

    /**
     * Gets if the equipment of the player drops.
     *
     * @return True if the equipment of the player drops.
     */
    public boolean isDropEquipment() {
        return dropEquipment;
    }

    /**
     * Sets if the equipment of the player drops.
     *
     * @param dropEquipment True if the equipment of the player should drop.
     */
    public void setDropEquipment(boolean dropEquipment) {
        this.dropEquipment = dropEquipment;
    }

    /**
     * Gets if the experience of the player drops.
     *
     * @return True if the experience of the player drops.
     */
    public boolean isDropExperience() {
        return dropExperience;
    }

    /**
     * Sets if the experience of the player drops.
     *
     * @param dropExperience True if the experience of the player should drop.
     */
    public void setDropExperience(boolean dropExperience) {
        this.dropExperience = dropExperience;
    }
}
