package com.combatreforged.metis.builder.mixin.world.scores;

import com.combatreforged.metis.api.world.scoreboard.ScoreboardTeam;
import com.combatreforged.metis.builder.extension.world.scores.PlayerTeamExtension;
import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.world.scoreboard.WrappedScoreboardTeam;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTeam.class)
public abstract class PlayerTeamMixin implements Wrap<ScoreboardTeam>, PlayerTeamExtension {
    @Shadow @Final private Scoreboard scoreboard;
    private WrappedScoreboardTeam wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedScoreboardTeam((PlayerTeam) (Object) this);
    }

    @Override
    public ScoreboardTeam wrap() {
        return wrapped;
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }
}
