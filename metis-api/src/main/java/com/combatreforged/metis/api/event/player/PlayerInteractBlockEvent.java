package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.block.Block;
import com.combatreforged.metis.api.world.entity.equipment.HandSlot;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.ItemStack;
import com.combatreforged.metis.api.world.util.Location;

/**
 * Calls when a player interacts at a block
 */
public class PlayerInteractBlockEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerInteractBlockEvent> BACKEND = EventBackend.create(PlayerInteractBlockEvent.class);

    private boolean cancelled;

    private final Block block;
    private final Location location;
    private final HandSlot hand;
    private final ItemStack itemStack;

    public PlayerInteractBlockEvent(Player player, Block block, Location location, HandSlot hand, ItemStack itemStack) {
        super(player);
        this.block = block;
        this.location = location;
        this.hand = hand;
        this.itemStack = itemStack;
    }

    /**
     * Returns the block the player is interacting with.
     *
     * @return the block the player is interacting with
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Returns the location of the block.
     *
     * @return the location of the block
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the hand the player is using.
     *
     * @return the hand the player is using
     */
    public HandSlot getHand() {
        return hand;
    }

    /**
     * Returns the itemstack the player is using.
     *
     * @return the itemstack the player is using
     */
    public ItemStack getItemStack() {
        return itemStack;
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
