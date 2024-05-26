package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.block.Block;
import com.combatreforged.metis.api.world.block.BlockState;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.util.Location;

/**
 * Calls when a player changed the state of a block.
 */
public class PlayerChangeBlockStateEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerChangeBlockStateEvent> BACKEND = EventBackend.create(PlayerChangeBlockStateEvent.class);

    private boolean cancelled;

    private final Location location;
    private final Block currentBlockState;
    private BlockState newBlockState;
    private final Action action;

    public PlayerChangeBlockStateEvent(Player player, Location location, Block currentBlockState, BlockState newBlockState, Action action) {
        super(player);
        this.location = location;
        this.currentBlockState = currentBlockState;
        this.newBlockState = newBlockState;
        this.action = action;
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
     * Returns the old state of the block.
     *
     * @return the old state of the block
     */
    public Block getCurrentBlockState() {
        return currentBlockState;
    }

    /**
     * Retusn the new state of the block.
     *
     * @return the new state of the block
     */
    public BlockState getNewBlockState() {
        return newBlockState;
    }

    /**
     * Sets the new state of the block.
     *
     * @param newBlockState the new state of the block
     */
    public void setNewBlockState(BlockState newBlockState) {
        this.newBlockState = newBlockState;
    }

    /**
     * Returns the action the player is doing to change the blockstate.
     *
     * @return the action the player is doing to change the blockstate
     */
    public Action getAction() {
        return action;
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
     * An enum to specify the action a player is doing to change a blockstate.
     */
    public enum Action {
        /** When a player opens or closes a door */
        OPEN_CLOSE,
        /** When a player strips a block */
        STRIP_BLOCK,
        /** When a player pulls a lever */
        PULL_LEVER,
        /** When a player presses a button */
        PRESS_BUTTON,
        /** When a player walks on farmland and destroys it */
        TRAMPLE_ON_FARMLAND
    }
}
