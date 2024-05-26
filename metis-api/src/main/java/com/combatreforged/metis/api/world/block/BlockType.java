package com.combatreforged.metis.api.world.block;

import com.combatreforged.metis.api.entrypoint.interfaces.Namespaced;
import com.combatreforged.metis.api.entrypoint.interfaces.ObjectMapped;
import com.combatreforged.metis.api.entrypoint.interfaces.StringIdentified;
import com.google.errorprone.annotations.Immutable;

@Immutable
public interface BlockType extends ObjectMapped, Namespaced {
    @Immutable
    abstract class Other implements BlockType, StringIdentified {
    }
}
