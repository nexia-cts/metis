package com.combatreforged.metis.builder.mixin_interfaces;

import net.minecraft.world.inventory.ContainerLevelAccess;

public interface LevelAccessOwner {
    void setContainerLevelAccess(ContainerLevelAccess access);
    ContainerLevelAccess getContainerLevelAccess();
}
