package com.combatreforged.metis.builder.extension.world.entity;

public interface EntityExtension {
    int invokeGetPermissionLevel();

    boolean injectChangeMovementStateEvent();
    void setInjectMovementStateEvent(boolean inject);
}
