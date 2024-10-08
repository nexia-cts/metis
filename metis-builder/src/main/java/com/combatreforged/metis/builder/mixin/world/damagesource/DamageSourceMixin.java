package com.combatreforged.metis.builder.mixin.world.damagesource;

import com.combatreforged.metis.api.world.damage.DamageData;
import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.util.ObjectMappings;
import com.combatreforged.metis.builder.implementation.world.damage.WrappedDamageData;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin implements Wrap<DamageData> {
    private DamageData wrapped;

    @SuppressWarnings("unused")
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void loadConversionTable(@SuppressWarnings("unused") CallbackInfo ci) {
        ObjectMappings.setupDamageTypes();
    }

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrap(CallbackInfo ci) {
        this.wrapped = new WrappedDamageData((DamageSource) (Object) this);
    }

    @Override
    public DamageData wrap() {
        return wrapped;
    }
}
