package com.combatreforged.metis.api;

import com.combatreforged.metis.api.builder.Builder;
import com.combatreforged.metis.api.event.server.ServerTickEvent;
import com.combatreforged.metis.api.scheduler.TaskScheduler;
import com.combatreforged.metis.api.scheduler.TickFunction;
import com.combatreforged.metis.api.util.ImplementationUtils;
import com.combatreforged.metis.api.world.util.Pair;

public class MetisAPI {
    private static MetisAPI INSTANCE = null;

    private final Builder builder;
    private final ImplementationUtils implementationUtils;
    private final MetisServer server;
    private final TaskScheduler scheduler;
    private final TickFunction tickFunction;

    public MetisAPI(MetisServer server, Builder builder) {
        this.builder = builder;
        this.implementationUtils = builder.createImplementationUtils();
        this.server = server;
        Pair<TaskScheduler, TickFunction> sched = TaskScheduler.create();
        this.scheduler = sched.a();
        this.tickFunction = sched.b();

        ServerTickEvent.BACKEND.register(event -> event.runAfterwards(tickFunction::tick));

        INSTANCE = this;
    }

    public Builder getBuilder() {
        return builder;
    }

    public MetisServer getServer() {
        return server;
    }

    public ImplementationUtils getImplementationUtils() {
        return implementationUtils;
    }

    public static MetisAPI getInstance() {
        return INSTANCE;
    }

    public TaskScheduler getScheduler() {
        return scheduler;
    }
}
