package com.combatreforged.metis.builder.implementation.world.entity;

import com.combatreforged.metis.api.world.entity.LivingEntity;
import com.combatreforged.metis.api.world.entity.Mob;
import com.combatreforged.metis.builder.implementation.Wrapped;

public class WrappedMob extends WrappedLivingEntity implements Mob {
    public WrappedMob(net.minecraft.world.entity.Mob wrappedMob) {
        super(wrappedMob);
    }

    @Override
    public LivingEntity getTargetEntity() {
        return Wrapped.wrap(wrappedMob().getTarget(), WrappedLivingEntity.class);
    }

    @Override
    public void setTargetEntity(LivingEntity target) {
        wrappedMob().setTarget(((WrappedLivingEntity) target).unwrap());
    }

    @Override
    public net.minecraft.world.entity.Mob unwrap() {
        return this.wrappedMob();
    }

    private net.minecraft.world.entity.Mob wrappedMob() {
        return (net.minecraft.world.entity.Mob) this.wrapped;
    }
}
