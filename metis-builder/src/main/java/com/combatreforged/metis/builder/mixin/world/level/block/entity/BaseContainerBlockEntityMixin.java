package com.combatreforged.metis.builder.mixin.world.level.block.entity;

import com.combatreforged.metis.api.world.block.container.BlockEntityContainer;
import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.world.item.container.WrappedBlockEntityContainer;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseContainerBlockEntity.class)
public class BaseContainerBlockEntityMixin implements Wrap<BlockEntityContainer> {
    private WrappedBlockEntityContainer wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedBlockEntityContainer((BaseContainerBlockEntity) (Object) this);
    }

    @Override
    public BlockEntityContainer wrap() {
        return wrapped;
    }
}
