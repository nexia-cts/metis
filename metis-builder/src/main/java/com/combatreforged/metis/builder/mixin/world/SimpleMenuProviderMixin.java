package com.combatreforged.metis.builder.mixin.world;

import com.combatreforged.metis.api.world.item.container.menu.MenuHolder;
import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.world.item.container.menu.WrappedMenuHolder;
import net.minecraft.world.SimpleMenuProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleMenuProvider.class)
public abstract class SimpleMenuProviderMixin implements Wrap<MenuHolder> {
    WrappedMenuHolder wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        wrapped = new WrappedMenuHolder((SimpleMenuProvider) (Object) this);
    }

    @Override
    public MenuHolder wrap() {
        return wrapped;
    }
}
