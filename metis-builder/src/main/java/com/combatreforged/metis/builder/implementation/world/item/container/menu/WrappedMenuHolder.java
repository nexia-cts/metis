package com.combatreforged.metis.builder.implementation.world.item.container.menu;

import com.combatreforged.metis.api.world.item.container.menu.MenuHolder;
import com.combatreforged.metis.builder.implementation.Wrapped;
import net.minecraft.world.SimpleMenuProvider;

public class WrappedMenuHolder extends Wrapped<SimpleMenuProvider> implements MenuHolder {
    public WrappedMenuHolder(SimpleMenuProvider wrapped) {
        super(wrapped);
    }
}
