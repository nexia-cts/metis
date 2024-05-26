package com.combatreforged.metis.api.entrypoint;

import com.combatreforged.metis.api.MetisAPI;
import com.combatreforged.metis.api.MetisServer;

public interface MetisPlugin {
    void onMetisLoad(MetisAPI api, MetisServer server);
}
