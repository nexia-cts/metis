package com.combatreforged.metis.api;

import com.combatreforged.metis.api.command.CommandSender;
import com.combatreforged.metis.api.command.CommandSourceInfo;
import com.combatreforged.metis.api.util.Identifier;
import com.combatreforged.metis.api.world.World;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.scoreboard.Scoreboard;
import com.mojang.brigadier.CommandDispatcher;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MetisServer extends CommandSender {
    int getMaxPlayerCount();
    Collection<Player> getPlayers();
    Player getPlayer(String name);
    Player getPlayer(UUID uuid);
    Collection<World> getWorlds();
    World getWorld(Identifier identifier);
    Scoreboard getServerScoreboard();
    CommandDispatcher<CommandSourceInfo> getCommandDispatcher();

    void stopServer();

    boolean hasWorld(String name);
    void loadWorld(Path path);
    void loadWorld(Path path, String name);
    CompletableFuture<Void> loadWorldAsync(Path path);
    CompletableFuture<Void> loadWorldAsync(Path path, String name);
    void saveWorld(String name);
    void unloadWorld(String name) throws IOException;
    void unloadWorld(String name, boolean save) throws IOException;
}
