package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.util.Location;

/**
 * Calls when a player is moving.
 */
public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerMoveEvent> BACKEND = EventBackend.create(PlayerMoveEvent.class);

    private final Location oldPosition;
    private Location newPosition;
    private boolean cancelled;

    public PlayerMoveEvent(Player player, Location oldPosition, Location newPosition, boolean cancelled) {
        super(player);
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.cancelled = cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Returns the old position of the player.
     *
     * @return the old position of the player
     */
    public Location getOldPosition() {
        return oldPosition;
    }

    /**
     * Returns the new position of the player.
     *
     * @return the new position of the player
     */
    public Location getNewPosition() {
        return newPosition;
    }

    /**
     * Sets the new position of the player.
     *
     * @param newPosition the new position of the player
     */
    public void setNewPosition(Location newPosition) {
        this.newPosition = newPosition;
    }
}
