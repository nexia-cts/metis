package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;

/**
 * Calls if a players foodlevel changes
 */
public class PlayerFoodLevelsChangeEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerFoodLevelsChangeEvent> BACKEND = EventBackend.create(PlayerFoodLevelsChangeEvent.class);

    private boolean cancelled;

    private float saturation;
    private int foodLevel;

    public PlayerFoodLevelsChangeEvent(Player player, float saturation, int foodLevel) {
        super(player);
        this.saturation = saturation;
        this.foodLevel = foodLevel;
    }

    /**
     * Returns the saturation the player has.
     *
     * @return the saturation the player has
     */
    public float getSaturation() {
        return saturation;
    }

    /**
     * Sets the saturation the player should have.
     *
     * @param saturation the saturation the player should have
     */
    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    /**
     * Returns the foodlevel the player has.
     *
     * @return the foodlevel the player has
     */
    public int getFoodLevel() {
        return foodLevel;
    }

    /**
     * Sets the foodLevel the player should have.
     *
     * @param foodLevel the foodLevel the player should have
     */
    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
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
