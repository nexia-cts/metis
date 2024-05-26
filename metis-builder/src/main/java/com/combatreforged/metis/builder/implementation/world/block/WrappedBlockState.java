package com.combatreforged.metis.builder.implementation.world.block;

import com.combatreforged.metis.api.world.util.Location;
import net.minecraft.world.level.block.state.BlockState;

public class WrappedBlockState extends WrappedBlock implements com.combatreforged.metis.api.world.block.BlockState {
    private BlockState blockState;

    public WrappedBlockState(Location location, BlockState blockState) {
        super(location);
        this.blockState = blockState;
    }

    @Override
    public void update(BlockState state) {
        this.blockState = state;
    }

    @Override
    public BlockState state() {
        return this.blockState;
    }
}
