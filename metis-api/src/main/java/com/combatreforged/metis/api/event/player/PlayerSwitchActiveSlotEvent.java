package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;

/**
 * Calls when the player switches his selected slot in hotbar.
 */
public class PlayerSwitchActiveSlotEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerSwitchActiveSlotEvent> BACKEND = EventBackend.create(PlayerSwitchActiveSlotEvent.class);

    private boolean cancelled;
    private int newSlot;

    public PlayerSwitchActiveSlotEvent(Player player, int newSlot) {
        super(player);
        this.newSlot = newSlot;
    }

    /**
     * Returns the new slot the player selected.
     *
     * @return the new slot the player selected
     */
    public int getNewSlot() {
        return newSlot;
    }

    /**
     * Sets the new selected slot for the player.
     *
     * @param newSlot the new selected slot for the player
     */
    public void setNewSlot(int newSlot) {
        if (newSlot < 0 || newSlot > 8) {
            throw new IllegalArgumentException("New slot must be between 0-8");
        }
        this.newSlot = newSlot;
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
