package com.combatreforged.metis.api.world;

import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.util.Identifier;
import com.combatreforged.metis.api.world.block.Block;
import com.combatreforged.metis.api.world.block.BlockEntity;
import com.combatreforged.metis.api.world.block.BlockState;
import com.combatreforged.metis.api.world.border.WorldBorder;
import com.combatreforged.metis.api.world.entity.Entity;
import com.combatreforged.metis.api.world.entity.player.GameModeType;
import com.combatreforged.metis.api.world.util.BoundingBox;
import com.combatreforged.metis.api.world.util.Location;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface World {
    Identifier getIdentifier();

    Block getBlockAt(Location location);
    void setBlockAt(Location location, BlockState state);
    default boolean isBlockEntity(Location location) {
        return getBlockEntity(location) != null;
    }
    BlockEntity getBlockEntity(Location location);
    List<Entity> getEntities();

    default List<Entity> getEntities(BoundingBox area) {
        return getEntities(area, (entity) -> true);
    }
    List<Entity> getEntities(BoundingBox area, Predicate<Entity> filter);

    boolean spawn(Entity entity);

    Entity getEntity(UUID uuid);

    MetisServer getServer();

    boolean isSubWorld();

    default boolean isRaining() {
        return getWeather() == Weather.RAIN;
    }
    default boolean isThundering() {
        return getWeather() == Weather.THUNDER;
    }

    Weather getWeather();
    void setWeather(Weather weather);
    void setWeather(Weather weather, int duration);

    WorldBorder getWorldBorder();

    long getDayTime();
    void setDayTime(long time);
    long getGameTime();
    void setGameTime(long time);

    GameModeType getDefaultGameMode();
    void setDefaultGameMode(GameModeType gameMode);
}
