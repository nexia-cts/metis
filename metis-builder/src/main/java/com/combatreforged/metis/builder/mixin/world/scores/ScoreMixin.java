package com.combatreforged.metis.builder.mixin.world.scores;

import com.combatreforged.metis.api.world.scoreboard.ScoreboardScore;
import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.world.scoreboard.WrappedScoreboardScore;
import net.minecraft.world.scores.Score;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Score.class)
public abstract class ScoreMixin implements Wrap<ScoreboardScore> {
    private WrappedScoreboardScore wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedScoreboardScore((Score) (Object) this);
    }

    @Override
    public ScoreboardScore wrap() {
        return wrapped;
    }
}
