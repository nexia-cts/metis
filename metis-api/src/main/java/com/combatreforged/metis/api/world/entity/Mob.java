package com.combatreforged.metis.api.world.entity;

public interface Mob extends LivingEntity {
    LivingEntity getTargetEntity();
    void setTargetEntity(LivingEntity target);
}
