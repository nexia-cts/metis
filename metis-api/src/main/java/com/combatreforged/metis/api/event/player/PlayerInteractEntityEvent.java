package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.Entity;
import com.combatreforged.metis.api.world.entity.player.Player;

/**
 * Calls when a player interacts with an entity
 */
public class PlayerInteractEntityEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerInteractEntityEvent> BACKEND = EventBackend.create(PlayerInteractEntityEvent.class);

    private boolean cancelled;
    private final Entity entity;

    public PlayerInteractEntityEvent(Player player, Entity entity) {
        super(player);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
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
