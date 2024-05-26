package com.combatreforged.metis.api.event.server;

import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.event.EventBackend;

/**
 * This event is called when the server ticks.
 */
public class ServerTickEvent extends ServerEvent {
    public static final EventBackend<ServerTickEvent> BACKEND = EventBackend.create(ServerTickEvent.class);

    private final int tickID;

    public ServerTickEvent(MetisServer server, int tickID) {
        super(server);
        this.tickID = tickID;
    }

    /**
     * Retuns the id of the server tick.
     *
     * @return id of the tick
     */
    public int getTickID() {
        return tickID;
    }
}
