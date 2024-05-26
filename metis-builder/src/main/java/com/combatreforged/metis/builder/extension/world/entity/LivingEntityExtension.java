package com.combatreforged.metis.builder.extension.world.entity;

import com.combatreforged.metis.api.event.entity.LivingEntityDeathEvent;

public interface LivingEntityExtension {
    boolean willDropItems();

    void setDeathEvent(LivingEntityDeathEvent event);
    LivingEntityDeathEvent getDeathEvent();
}
