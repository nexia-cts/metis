package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.container.menu.ContainerMenu;

/**
 * Calls when a player closes an inventory.
 */
public class PlayerCloseContainerEvent extends PlayerEvent implements Cancellable {
    public static EventBackend<PlayerCloseContainerEvent> BACKEND = EventBackend.create(PlayerCloseContainerEvent.class);

    private boolean cancelled;
    private final ContainerMenu closedContainer;

    public PlayerCloseContainerEvent(Player player, ContainerMenu closedContainer) {
        super(player);
        this.closedContainer = closedContainer;
    }

    /**
     * Returns the container which is closed.
     *
     * @return the container which is closed
     */
    public ContainerMenu getClosedContainer() {
        return closedContainer;
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
