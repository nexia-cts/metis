package com.combatreforged.metis.api.world.item;

import com.combatreforged.metis.api.entrypoint.interfaces.Namespaced;
import com.combatreforged.metis.api.entrypoint.interfaces.ObjectMapped;
import com.combatreforged.metis.api.entrypoint.interfaces.StringIdentified;

public interface Enchantment extends Namespaced, ObjectMapped {
    boolean isCurse();

    int getMaxLevel();

    boolean canBeAppliedTo(ItemStack itemStack);

    abstract class Other implements Enchantment, StringIdentified {
    }
}
