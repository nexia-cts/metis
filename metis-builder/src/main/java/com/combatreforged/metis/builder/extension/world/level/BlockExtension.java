package com.combatreforged.metis.builder.extension.world.level;

import com.combatreforged.metis.api.event.player.PlayerBreakBlockEvent;

public interface BlockExtension {
    void currentBreakEvent(PlayerBreakBlockEvent playerBreakBlockEvent);
}
