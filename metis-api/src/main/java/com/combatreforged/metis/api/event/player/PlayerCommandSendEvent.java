package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;

/**
 * Calls when a player executes a command
 */
public class PlayerCommandSendEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerCommandSendEvent> BACKEND = EventBackend.create(PlayerCommandSendEvent.class);

    private boolean cancelled;
    private String command;

    public PlayerCommandSendEvent(Player player, String command) {
        super(player);
        this.command = command;
    }

    /**
     * Returns the string the player types in chat.
     *
     * @return the string the player types in chat
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Sets the string the player types in chat.
     *
     * @param command the string the player should type in chat
     */
    public void setCommand(String command) {
        this.command = command;
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
