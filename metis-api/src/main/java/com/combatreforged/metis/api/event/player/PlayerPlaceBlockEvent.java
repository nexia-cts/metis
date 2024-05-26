package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.World;
import com.combatreforged.metis.api.world.block.Block;
import com.combatreforged.metis.api.world.block.BlockState;
import com.combatreforged.metis.api.world.entity.equipment.HandSlot;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.ItemStack;
import com.combatreforged.metis.api.world.util.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Calls when a player places a block.
 */
public class PlayerPlaceBlockEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerPlaceBlockEvent> BACKEND = EventBackend.create(PlayerPlaceBlockEvent.class);

    private boolean cancelled;

    private final Location location;
    private final @Nullable Block currentBlockState;
    private @Nullable BlockState newBlockState;
    private final ItemStack blockStack;
    private final HandSlot placingHand;

    public PlayerPlaceBlockEvent(Player player, Location location, @Nullable Block currentBlockState, @Nullable BlockState newBlockState, ItemStack blockStack, HandSlot placingHand) {
        super(player);
        this.location = location;
        this.currentBlockState = currentBlockState;
        this.newBlockState = newBlockState;
        this.blockStack = blockStack;
        this.placingHand = placingHand;
    }

    /**
     * Returns the location of the placed block.
     *
     * @return the location of the placed block
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the world where the event is happening.
     *
     * @return the world where the event is happening
     */
    public World getWorld() {
        return location.getWorld();
    }

    /**
     * Returns the old blockstate of the placed block.
     *
     * @return the old blockstate of the placed block
     */
    public @Nullable Block getCurrentBlockState() {
        return currentBlockState;
    }

    /**
     * Returns the new blockstate of the placed block.
     *
     * @return the new blockstate of the placed block
     */
    public @Nullable BlockState getNewBlockState() {
        return newBlockState;
    }

    /**
     * Sets the new state the block should have.
     *
     * @param newBlockState the new state the block should have
     */
    public void setNewBlockState(@Nullable BlockState newBlockState) {
        this.newBlockState = newBlockState;
    }

    /**
     * Returns the itemstack used to place the block.
     *
     * @return the itemstack used to place the block
     */
    public ItemStack getBlockStack() {
        return blockStack;
    }

    /**
     * Returns the hand the player used to place the block.
     *
     * @return the hand the player used to place the block
     */
    public HandSlot getPlacingHand() {
        return placingHand;
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
