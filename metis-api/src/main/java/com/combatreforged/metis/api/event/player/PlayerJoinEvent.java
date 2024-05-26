package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;
import net.kyori.adventure.text.Component;

/**
 * Calls when a player is joining the server.
 */
public class PlayerJoinEvent extends PlayerEvent {
    public static final EventBackend<PlayerJoinEvent> BACKEND = EventBackend.create(PlayerJoinEvent.class);

    private Component joinMessage;

    public PlayerJoinEvent(Player player, Component joinMessage) {
        super(player);
        this.joinMessage = joinMessage;
    }

    /**
     * Returns the join message shown in chat.
     *
     * @return the join message shown in chat
     */
    public Component getJoinMessage() {
        return joinMessage;
    }


    /**
     * Sets the join message shown in chat.
     *
     * @param joinMessage the join message shown in chat (null to skip the message)
     */
    public void setJoinMessage(Component joinMessage) {
        this.joinMessage = joinMessage;
    }
}
