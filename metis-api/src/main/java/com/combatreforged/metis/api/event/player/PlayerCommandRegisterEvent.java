package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.command.CommandType;
import com.combatreforged.metis.api.world.entity.player.Player;

import java.util.ArrayList;

/**
 * Calls when the server sends the available commands to a player
 */
public class PlayerCommandRegisterEvent extends PlayerEvent {
    public static final EventBackend<PlayerCommandRegisterEvent> BACKEND = EventBackend.create(PlayerCommandRegisterEvent.class);

    private ArrayList<CommandType> blockedCommands = new ArrayList<>();
    private ArrayList<String> blockedCustomCommands = new ArrayList<>();

    public PlayerCommandRegisterEvent(Player player) {
        super(player);
    }

    /**
     * Sets the commands that should NOT be send to the player.
     *
     * @param blockedCommands the commands that should NOT be send to the player
     */
    public void setBlockedCommands(ArrayList<CommandType> blockedCommands) {
        this.blockedCommands = blockedCommands;
    }

    /**
     * Sets the commands that should NOT be send to the player.
     *
     * @return  the commands that should NOT be send to the player
     */
    public ArrayList<CommandType> getBlockedCommands() {
        return blockedCommands;
    }

    /**
     * Sets the custom commands that should NOT be send to the player.
     *
     * @param blockedCustomCommands the custom commands that should NOT be send to the player
     */
    public void setBlockedCustomCommands(ArrayList<String> blockedCustomCommands) {
        this.blockedCustomCommands = blockedCustomCommands;
    }

    /**
     * Sets the custom commands that should NOT be send to the player.
     *
     * @return  the custom commands that should NOT be send to the player
     */
    public ArrayList<String> getBlockedCustomCommands() {
        return blockedCustomCommands;
    }
}
