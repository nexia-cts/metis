package com.combatreforged.metis.builder.mixin.world.inventory;

import com.combatreforged.metis.builder.mixin_interfaces.LevelAccessOwner;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin implements LevelAccessOwner {
    @Override
    public void setContainerLevelAccess(ContainerLevelAccess access) {

    }

    @Override
    public ContainerLevelAccess getContainerLevelAccess() {
        return ContainerLevelAccess.NULL;
    }
}
