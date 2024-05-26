package com.combatreforged.metis.api.world.entity.player;

import com.combatreforged.metis.api.entrypoint.interfaces.ObjectMapped;
import com.combatreforged.metis.api.entrypoint.interfaces.StringIdentified;

public interface GameModeType extends ObjectMapped {
    abstract class Other implements GameModeType, StringIdentified {
        @Override
        public String toString() {
            return this.getId();
        }
    }
}
