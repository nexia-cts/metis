package com.combatreforged.metis.builder.mixin.world.inventory;

import com.combatreforged.metis.builder.mixin_interfaces.LevelAccessOwner;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.LoomMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LoomMenu.class)
public abstract class LoomMenuMixin implements LevelAccessOwner {
    @Mutable
    @Shadow
    @Final
    private ContainerLevelAccess access;

    @Override
    public void setContainerLevelAccess(ContainerLevelAccess access) {
        this.access = access;
    }

    @Override
    public ContainerLevelAccess getContainerLevelAccess() {
        return access;
    }
}
