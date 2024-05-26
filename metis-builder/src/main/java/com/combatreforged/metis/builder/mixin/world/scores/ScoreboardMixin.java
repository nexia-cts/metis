package com.combatreforged.metis.builder.mixin.world.scores;

import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.world.scoreboard.WrappedScoreboard;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Scoreboard.class)
public abstract class ScoreboardMixin implements Wrap<com.combatreforged.metis.api.world.scoreboard.Scoreboard> {
    private WrappedScoreboard wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedScoreboard((Scoreboard) (Object) this);
    }

    @Override
    public com.combatreforged.metis.api.world.scoreboard.Scoreboard wrap() {
        return wrapped;
    }
}
