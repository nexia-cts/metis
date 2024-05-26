package com.combatreforged.metis.api.world.sound;

import com.combatreforged.metis.api.entrypoint.interfaces.Namespaced;
import com.combatreforged.metis.api.entrypoint.interfaces.ObjectMapped;
import com.combatreforged.metis.api.entrypoint.interfaces.StringIdentified;

public interface SoundType extends ObjectMapped, Namespaced {
    abstract class Other implements com.combatreforged.metis.api.world.sound.SoundType, StringIdentified {
        @Override
        public String toString() {
            return this.getId();
        }
    }
}
