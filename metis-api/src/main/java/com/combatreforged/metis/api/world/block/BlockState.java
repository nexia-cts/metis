package com.combatreforged.metis.api.world.block;

import com.combatreforged.metis.api.builder.Builder;
import com.combatreforged.metis.api.world.util.Location;

public interface BlockState extends Block {
    static BlockState create(BlockType type, Location location) {
        return Builder.getInstance().createBlockState(type, location);
    }

    static BlockState of(Block block) {
        return Builder.getInstance().blockStateOfBlock(block);
    }
}
