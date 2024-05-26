package com.combatreforged.metis.builder.prelaunch;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class MetisBuilderPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        System.out.println("Loading version 1.0.0 of MetisBuilder!");
    }
}
