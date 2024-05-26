package com.combatreforged.metis.builder.mixin.world.level;

import com.combatreforged.metis.builder.implementation.util.ObjectMappings;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameType.class)
public abstract class GameTypeMixin {
    @Inject(method = "<clinit>", at = @At("TAIL")) @SuppressWarnings("unused")
    private static void loadConversionTable(CallbackInfo ci) {
        ObjectMappings.setupGameModes();
    }
}
