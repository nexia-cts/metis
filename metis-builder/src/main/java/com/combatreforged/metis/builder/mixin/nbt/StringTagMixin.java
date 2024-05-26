package com.combatreforged.metis.builder.mixin.nbt;

import com.combatreforged.metis.api.world.nbt.NBTValue;
import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.world.nbt.WrappedNBTValue;
import net.minecraft.nbt.StringTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StringTag.class)
public abstract class StringTagMixin implements Wrap<NBTValue> {
    private WrappedNBTValue wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrapped(String string, CallbackInfo ci) {
        this.wrapped = new WrappedNBTValue((StringTag) (Object) this);
    }

    @Override
    public NBTValue wrap() {
        return wrapped;
    }
}
