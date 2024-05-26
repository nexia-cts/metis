package com.combatreforged.metis.api.world.item;

import com.combatreforged.metis.api.entrypoint.interfaces.Namespaced;
import com.combatreforged.metis.api.entrypoint.interfaces.ObjectMapped;
import com.combatreforged.metis.api.entrypoint.interfaces.StringIdentified;
import com.combatreforged.metis.api.world.block.BlockType;

public interface ItemType extends ObjectMapped, Namespaced {
    boolean isBlockItem();
    BlockType getBlock();
    int getMaxStackSize();
    int getMaxDamage();

    abstract class Other implements ItemType, StringIdentified {}
}
