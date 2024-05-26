package com.combatreforged.metis.api.world.effect;

import com.combatreforged.metis.api.entrypoint.interfaces.Namespaced;
import com.combatreforged.metis.api.entrypoint.interfaces.ObjectMapped;
import com.combatreforged.metis.api.entrypoint.interfaces.StringIdentified;

public interface StatusEffect extends ObjectMapped, Namespaced {
    Type getType();

    enum Type {
        BENEFICIAL, HARMFUL, NEUTRAL
    }

    abstract class Other implements StatusEffect, StringIdentified {
    }
}
