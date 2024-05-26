package com.combatreforged.metis.api.world.item.container.menu;

import com.combatreforged.metis.api.entrypoint.interfaces.ObjectMapped;
import net.kyori.adventure.text.Component;

public interface ContainerMenuType extends ObjectMapped {
    MenuHolder createMenu(Component title);
}
