package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.equipment.HandSlot;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.ItemStack;

/**
 * Calls when a player uses an item.
 */
public class PlayerUseItemEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerUseItemEvent> BACKEND = EventBackend.create(PlayerUseItemEvent.class);

    private boolean cancelled;
    private final ItemStack itemStack;
    private final HandSlot hand;

    public PlayerUseItemEvent(Player player, ItemStack itemStack, HandSlot hand) {
        super(player);
        this.itemStack = itemStack;
        this.hand = hand;
    }

    /**
     * Returns the itemstack the player is using.
     *
     * @return the itemstack the player is using
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Returns the hand the player is using.
     *
     * @return the hand the player is using
     */
    public HandSlot getHand() {
        return hand;
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
