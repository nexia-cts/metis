package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.Entity;
import com.combatreforged.metis.api.world.entity.equipment.HandSlot;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.util.Location;

/**
 * Calls when a player interacts at an entity
 */
public class PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {
    public static final EventBackend<PlayerInteractAtEntityEvent> BACKEND = EventBackend.create(PlayerInteractAtEntityEvent.class);

    private final Location interactionLocation;
    private final HandSlot hand;

    public PlayerInteractAtEntityEvent(Player player, Entity entity, Location interactionLocation, HandSlot hand) {
        super(player, entity);
        this.interactionLocation = interactionLocation;
        this.hand = hand;
    }

    /**
     * Returns the localtion of the interaction.
     *
     * @return the localtion of the interaction
     */
    public Location getInteractionLocation() {
        return interactionLocation;
    }

    /**
     * Returns the handslot the player is using.
     *
     * @return the handslot the player is using
     */
    public HandSlot getHand() {
        return hand;
    }
}
