package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.container.menu.ContainerMenu;
import org.jetbrains.annotations.Nullable;

/**
 * Calls when a player opens a container.
 */
public class PlayerOpenContainerEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerOpenContainerEvent> BACKEND = EventBackend.create(PlayerOpenContainerEvent.class);

    private boolean cancelled;
    private final @Nullable ContainerMenu newMenu;

    public PlayerOpenContainerEvent(Player player, @Nullable ContainerMenu newMenu) {
        super(player);
        this.newMenu = newMenu;
    }

    /**
     * Returns the container which is opened.
     *
     * @return the container which is opened
     */
    public @Nullable ContainerMenu getNewMenu() {
        return newMenu;
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
