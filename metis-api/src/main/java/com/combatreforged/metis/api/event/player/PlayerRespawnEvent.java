package com.combatreforged.metis.api.event.player;

import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.entity.player.GameModeType;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.util.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Calls when a player respawns.
 */
public class PlayerRespawnEvent extends PlayerEvent {
    public static final EventBackend<PlayerRespawnEvent> BACKEND = EventBackend.create(PlayerRespawnEvent.class);

    @Nullable private Location spawnpoint;
    private boolean spawnpointForced;
    private GameModeType respawnMode;

    public PlayerRespawnEvent(Player player, @Nullable Location spawnpoint, boolean spawnpointForced, GameModeType respawnMode) {
        super(player);
        this.spawnpoint = spawnpoint;
        this.spawnpointForced = spawnpointForced;
        this.respawnMode = respawnMode;
    }

    /**
     * Returns the location where the player respawns.
     *
     * @return the location where the player respawns
     */
    @Nullable
    public Location getSpawnpoint() {
        return spawnpoint;
    }

    /**
     * Sets the location where the player should respawn.
     *
     * @param spawnpoint the location where the player should respawn
     */
    public void setSpawnpoint(@Nullable Location spawnpoint) {
        this.spawnpoint = spawnpoint;
    }

    /**
     * Returns if the spawnpoint is forced.
     *
     * @return true if the spawnpoint is forced
     */
    public boolean isSpawnpointForced() {
        return spawnpointForced;
    }

    /**
     * Sets if the spawnpoint is forced.
     *
     * @param spawnpointForced true if the spawnpoint is forced
     */
    public void setSpawnpointForced(boolean spawnpointForced) {
        this.spawnpointForced = spawnpointForced;
    }

    /**
     * Returns the gamemode in which the player respawns.
     *
     * @return the gamemode in which the player respawns
     */
    public GameModeType getRespawnMode() {
        return respawnMode;
    }

    /**
     * Sets the gamemode the player should respawn in.
     *
     * @param respawnMode the gamemode the player should respawn ins
     */
    public void setRespawnMode(GameModeType respawnMode) {
        this.respawnMode = respawnMode;
    }
}
