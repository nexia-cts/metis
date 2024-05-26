package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.entity.LivingEntityEvent;
import com.combatreforged.metis.api.world.entity.player.Player;

/**
 * Represents a player related event.
 */
public abstract class PlayerEvent extends LivingEntityEvent {
    private final Player player;

    public PlayerEvent(Player player) {
        super(player);
        this.player = player;
    }

    /**
     * Returns the player involved in this event.
     *
     * @return the player involved in this event
     */
    public Player getPlayer() {
        return player;
    }
}
