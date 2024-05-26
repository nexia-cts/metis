package com.combatreforged.metis.api.world.block;

import com.combatreforged.metis.api.world.nbt.NBTObject;
import net.kyori.adventure.nbt.api.BinaryTagHolder;

public interface BlockEntity {
    Block getBlock();
    @Deprecated BinaryTagHolder getBlockData();
    @Deprecated void setBlockData(BinaryTagHolder tag);

    NBTObject getBlockNBT();
    void setBlockNBT(NBTObject nbt);
}
