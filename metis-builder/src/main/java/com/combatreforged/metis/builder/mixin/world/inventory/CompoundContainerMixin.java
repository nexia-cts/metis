package com.combatreforged.metis.builder.mixin.world.inventory;

import com.combatreforged.metis.api.world.item.container.Container;
import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.world.item.container.WrappedGenericContainer;
import net.minecraft.world.CompoundContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CompoundContainer.class)
public abstract class CompoundContainerMixin implements Wrap<Container> {
    WrappedGenericContainer wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedGenericContainer((CompoundContainer) (Object) this);
    }

    @Override
    public Container wrap() {
        return wrapped;
    }
}
