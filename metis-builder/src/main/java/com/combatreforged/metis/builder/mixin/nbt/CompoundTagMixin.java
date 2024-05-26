package com.combatreforged.metis.builder.mixin.nbt;

import com.combatreforged.metis.api.world.nbt.NBTObject;
import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.world.nbt.WrappedNBTObject;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CompoundTag.class)
public abstract class CompoundTagMixin implements Wrap<NBTObject> {
    private WrappedNBTObject wrapped;

    @Inject(method = "<init>(Ljava/util/Map;)V", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedNBTObject((CompoundTag) (Object) this);
    }

    @Override
    public NBTObject wrap() {
        return wrapped;
    }
}
