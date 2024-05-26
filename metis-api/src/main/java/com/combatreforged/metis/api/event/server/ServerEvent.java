package com.combatreforged.metis.api.event.server;

import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.event.Event;

/**
 * Miscellaneous server events.
 */
public abstract class ServerEvent extends Event {
    private final MetisServer server;

    public ServerEvent(MetisServer server) {
        this.server = server;
    }

    /**
     * Gets the server involved in this event.
     *
     * @return server involved in this event
     */
    public MetisServer getServer() {
        return server;
    }
}
