package com.combatreforged.metis.api.world.block.container;

import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.container.Container;
import com.combatreforged.metis.api.world.item.container.menu.ContainerMenu;
import net.kyori.adventure.text.Component;

public interface BlockEntityContainer extends Container {
    Component getName();
    void setName(Component component);

    ContainerMenu openToPlayer(Player player);
}
