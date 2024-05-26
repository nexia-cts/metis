package com.combatreforged.metis.api.util;

import com.combatreforged.metis.api.MetisAPI;
import com.combatreforged.metis.api.entrypoint.interfaces.Namespaced;
import com.combatreforged.metis.api.world.block.BlockType;
import com.combatreforged.metis.api.world.effect.StatusEffect;
import com.combatreforged.metis.api.world.item.Enchantment;
import com.combatreforged.metis.api.world.item.ItemStack;
import com.combatreforged.metis.api.world.item.ItemType;
import com.combatreforged.metis.api.world.item.container.menu.ContainerMenuType;
import com.combatreforged.metis.api.world.item.container.menu.MenuHolder;
import net.kyori.adventure.text.Component;

public interface ImplementationUtils {
    int getMaxLevel(Enchantment enchantment);
    boolean isCurse(Enchantment enchantment);
    boolean canApply(Enchantment enchantment, ItemStack itemStack);

    StatusEffect.Type getType(StatusEffect effect);

    boolean isBlockItem(ItemType item);
    BlockType getBlock(ItemType item);
    int getMaxStackSize(ItemType item);
    int getMaxDamage(ItemType item);

    Identifier getIdentifier(Namespaced namespaced);

    <T extends Namespaced> T getByIdentifier(Identifier identifier, Class<T> type);

    MenuHolder createMenu(ContainerMenuType type, Component title);

    static ImplementationUtils getInstance() {
        return MetisAPI.getInstance().getImplementationUtils();
    }
}
