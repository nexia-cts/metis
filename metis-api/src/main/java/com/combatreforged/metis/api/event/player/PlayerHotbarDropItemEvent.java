package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.ItemStack;

/**
 * Calls when a player drops an item while not being in inventory
 */
public class PlayerHotbarDropItemEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerHotbarDropItemEvent> BACKEND = EventBackend.create(PlayerHotbarDropItemEvent.class);

    private boolean cancelled;

    private final ItemStack itemStack;
    private final boolean dropEntireStack;
    private final int hotbarSlot;

    public PlayerHotbarDropItemEvent(Player player, ItemStack itemStack, boolean dropEntireStack, int hotbarSlot) {
        super(player);
        this.itemStack = itemStack;
        this.dropEntireStack = dropEntireStack;
        this.hotbarSlot = hotbarSlot;
    }

    /**
     * Returns the itemstack which is dropped.
     *
     * @return the itemstack which is dropped
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Returns if one item or the complete itemstack is dropped.
     *
     * @return true if the entire itemstack is dropped
     */
    public boolean isDropEntireStack() {
        return dropEntireStack;
    }

    /**
     * Returns the slot of the hotbar which is dropped.
     *
     * @return the slot of the hotbar which is dropped
     */
    public int getHotbarSlot() {
        return hotbarSlot;
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
