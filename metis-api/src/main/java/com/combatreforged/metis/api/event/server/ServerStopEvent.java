package com.combatreforged.metis.api.event.server;

import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.event.EventBackend;

/**
 * This event is called when either the server stop beginns.
 */
public class ServerStopEvent extends ServerEvent {
    public static final EventBackend<ServerStopEvent> BACKEND = EventBackend.create(ServerStopEvent.class);

    public ServerStopEvent(MetisServer server) {
        super(server);
    }

}
