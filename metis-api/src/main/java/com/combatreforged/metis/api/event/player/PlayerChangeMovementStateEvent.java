package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Called when a player changes his movement state.
 */
public class PlayerChangeMovementStateEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerChangeMovementStateEvent> BACKEND = EventBackend.create(PlayerChangeMovementStateEvent.class);

    private boolean cancelled;
    private final ChangedState changedState;
    private boolean changedValue;
    private final boolean previousValue;

    public PlayerChangeMovementStateEvent(Player player, ChangedState changedState, boolean changedValue) {
        super(player);
        this.changedState = changedState;
        this.changedValue = changedValue;
        this.previousValue = changedState.playerGetter.apply(player);
    }

    /**
     * Returns the changed state.
     *
     * @return the changed state
     */
    public ChangedState getChangedState() {
        return changedState;
    }

    /**
     * Returns the new value of the state.
     *
     * @return the new value of the state
     */
    public boolean getChangedValue() {
        return changedValue;
    }

    /**
     * Sets the new value of the state.
     *
     * @param changedValue the new value of the state
     */
    public void setChangedValue(boolean changedValue) {
        this.changedValue = changedValue;
    }

    /**
     * Returns the old value of the state.
     *
     * @return the old value of the state
     */
    public boolean getPreviousValue() {
        return previousValue;
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
     * An enum to specify the movement state of a player.
     */
    @SuppressWarnings("ImmutableEnumChecker")
    public enum ChangedState {
        /** When the player starts or stops to sprint */
        SPRINTING(Player::isSprinting, Player::setSprinting),
        /** When the player starts or stops to swim */
        SWIMMING(Player::isSwimming, Player::setSwimming),
        /** When the player starts or stops to sneak */
        SNEAKING(Player::isSneaking, Player::setSneaking),
        /** When the player starts or stops to fall */
        FALL_FLYING(Player::isFallFlying, Player::setFallFlying),
        /** When the player starts or stops to fly */
        FLYING(Player::isFlying, Player::setFlying);

        public final Function<Player, Boolean> playerGetter;
        public final BiConsumer<Player, Boolean> playerSetter;

        ChangedState(Function<Player, Boolean> playerGetter, BiConsumer<Player, Boolean> playerSetter) {
            this.playerGetter = playerGetter;
            this.playerSetter = playerSetter;
        }
    }
}
