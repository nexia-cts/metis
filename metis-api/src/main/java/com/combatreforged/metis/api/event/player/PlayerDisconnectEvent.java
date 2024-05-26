package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.Player;
import net.kyori.adventure.text.Component;

/**
 * Calls when a player disconnects from the server.
 */
public class PlayerDisconnectEvent extends PlayerEvent {
    public static final EventBackend<PlayerDisconnectEvent> BACKEND = EventBackend.create(PlayerDisconnectEvent.class);

    private final String disconnectReason;
    private Component leaveMessage;

    public PlayerDisconnectEvent(Player player, String disconnectReason, Component leaveMessage) {
        super(player);
        this.disconnectReason = disconnectReason;
        this.leaveMessage = leaveMessage;
    }

    /**
     * Returns the reason why the player is disconnected in a component.
     *
     * @return the reason why the player is disconnected in a component
     */
    public String getDisconnectReason() {
        return disconnectReason;
    }

    /**
     * Returns the message which is shows in chat that the player leaved.
     *
     * @return the message which is shows in chat that the player leaved
     */
    public Component getLeaveMessage() {
        return leaveMessage;
    }


    /**
     * Sets the message which is shows in chat that the player leaved.
     *
     * @param leaveMessage the message which shows is chat that the player leaved (null to skip the message)
     */
    public void setLeaveMessage(Component leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
}
