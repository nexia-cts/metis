package com.combatreforged.metis.builder.implementation.world.entity.monster;

import com.combatreforged.metis.api.world.entity.monster.Monster;
import com.combatreforged.metis.builder.implementation.world.entity.WrappedMob;

public class WrappedMonster extends WrappedMob implements Monster { ;
    public WrappedMonster(net.minecraft.world.entity.monster.Monster wrappedMonster) {
        super(wrappedMonster);
    }

    @Override
    public net.minecraft.world.entity.monster.Monster unwrap() {
        return wrappedMonster();
    }

    private net.minecraft.world.entity.monster.Monster wrappedMonster() {
        return (net.minecraft.world.entity.monster.Monster) this.wrapped;
    }
}
