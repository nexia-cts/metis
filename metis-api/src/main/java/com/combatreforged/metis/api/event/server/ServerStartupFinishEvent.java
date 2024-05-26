package com.combatreforged.metis.api.event.server;

import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.event.EventBackend;

/**
 * This event is called when the server startup has completed.
 */
public class ServerStartupFinishEvent extends ServerEvent {
    public static final EventBackend<ServerStartupFinishEvent> BACKEND = EventBackend.create(ServerStartupFinishEvent.class);

    public ServerStartupFinishEvent(MetisServer server) {
        super(server);
    }
}
