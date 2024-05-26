package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.World;
import com.combatreforged.metis.api.world.block.Block;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.ItemStack;
import com.combatreforged.metis.api.world.util.Location;

/**
 * Called when a block is broken by a player.
 */
public class PlayerBreakBlockEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerBreakBlockEvent> BACKEND = EventBackend.create(PlayerBreakBlockEvent.class);

    private boolean cancelled;

    private final Block block;
    private final Location location;
    private final ItemStack miningStack;
    private boolean dropBlock;

    public PlayerBreakBlockEvent(Player player, Block block, Location location, ItemStack miningStack, boolean dropBlock) {
        super(player);
        this.block = block;
        this.location = location;
        this.miningStack = miningStack;
        this.dropBlock = dropBlock;
    }

    /**
     * Returns the itemstack which the player used to break the block.
     *
     * @return the itemstack which the player used to break the block
     */
    public ItemStack getMiningStack() {
        return miningStack;
    }

    /**
     * Returns the block which is broken.
     *
     * @return the block which is broken
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Returns the localtion of the broken block.
     *
     * @return the localtion of the broken block
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the world where this event is happening.
     *
     * @return the world where this event is happening
     */
    public World getWorld() {
        return location.getWorld();
    }

    /**
     * Returns if the block is drops.
     *
     * @return True if the block drops
     */
    public boolean isDropBlock() {
        return dropBlock;
    }

    /**
     * Sets if the block should drop.
     *
     * @param dropBlock True if the block should drop
     */
    public void setDropBlock(boolean dropBlock) {
        this.dropBlock = dropBlock;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
