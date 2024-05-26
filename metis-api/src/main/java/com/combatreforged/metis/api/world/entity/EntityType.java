package com.combatreforged.metis.api.world.entity;

import com.combatreforged.metis.api.entrypoint.interfaces.Namespaced;
import com.combatreforged.metis.api.entrypoint.interfaces.ObjectMapped;
import com.combatreforged.metis.api.entrypoint.interfaces.StringIdentified;

public interface EntityType extends ObjectMapped, Namespaced {
    abstract class Other implements EntityType, StringIdentified {
        @Override
        public String toString() {
            return this.getId();
        }
    }
}
