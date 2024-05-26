package com.combatreforged.metis.builder.implementation;

import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.command.CommandSourceInfo;
import com.combatreforged.metis.api.util.Identifier;
import com.combatreforged.metis.api.world.World;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.scoreboard.Scoreboard;
import com.combatreforged.metis.builder.command.MetisCommand;
import com.combatreforged.metis.builder.extension.server.MinecraftServerExtension;
import com.combatreforged.metis.builder.implementation.util.ObjectMappings;
import com.combatreforged.metis.builder.implementation.world.WrappedWorld;
import com.combatreforged.metis.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.metis.builder.implementation.world.scoreboard.WrappedScoreboard;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import net.kyori.adventure.text.Component;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.storage.LevelStorageSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class WrappedMetisServer extends Wrapped<DedicatedServer> implements MetisServer {
    CommandDispatcher<CommandSourceInfo> commandDispatcher;

    public WrappedMetisServer(DedicatedServer wrapped) {
        super(wrapped);

        this.commandDispatcher = new CommandDispatcher<>();

        MetisCommand.register(this.getCommandDispatcher());
    }

    @Override
    public int getMaxPlayerCount() {
        return wrapped.getMaxPlayers();
    }

    @Override
    public List<Player> getPlayers() {
        return wrapped.getPlayerList().getPlayers()
                .stream()
                .map((player) -> Wrapped.wrap(player, WrappedPlayer.class))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public Player getPlayer(String name) {
        return Wrapped.wrap(wrapped.getPlayerList().getPlayerByName(name), WrappedPlayer.class);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return Wrapped.wrap(wrapped.getPlayerList().getPlayer(uuid), WrappedPlayer.class);
    }

    @Override
    public Collection<World> getWorlds() {
        Set<World> worlds = new HashSet<>();
        wrapped.getAllLevels().forEach((world) -> {
            worlds.add(Wrapped.wrap(world, WrappedWorld.class));
        });
        return worlds;
    }

    @Override
    public World getWorld(Identifier identifier) {
        return Wrapped.wrap(wrapped.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY,
                        new ResourceLocation(identifier.getNamespace(), identifier.getId()))), WrappedWorld.class);
    }

    @Override
    public Scoreboard getServerScoreboard() {
        return Wrapped.wrap(wrapped.getScoreboard(), WrappedScoreboard.class);
    }

    @Override
    public CommandDispatcher<CommandSourceInfo> getCommandDispatcher() {
        return commandDispatcher;
    }

    @Override
    public void stopServer() {
        wrapped.stopServer();
    }

    @Override
    public boolean hasWorld(String name) {
        return ((MinecraftServerExtension) wrapped).hasDynamicWorld(name);
    }

    @Override
    public void loadWorld(Path path) {
        this.loadWorld(path, path.getFileName().toString());
    }

    @Override
    public void loadWorld(Path path, String name) {
        try {
            ((MinecraftServerExtension) wrapped).loadDynamicWorldSync(name, new LevelStorageSource(path.resolve("../"), path.resolve("../backups/" + path.getFileName().toString() + "/"), DataFixers.getDataFixer()).createAccess(path.getFileName().toString()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not find level in specified Path '" + path.toAbsolutePath() + "'", e);
        }
    }

    @Override
    public CompletableFuture<Void> loadWorldAsync(Path path) {
        return this.loadWorldAsync(path, path.getFileName().toString());
    }

    @Override
    public CompletableFuture<Void> loadWorldAsync(Path path, String name) {
        try {
            return ((MinecraftServerExtension) wrapped).loadDynamicWorldAsync(name, new LevelStorageSource(path.resolve("../"), path.resolve("../backups/" + path.getFileName().toString() + "/"), DataFixers.getDataFixer()).createAccess(path.getFileName().toString()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not find level in specified Path '" + path.toAbsolutePath() + "'", e);
        }
    }

    @Override
    public void saveWorld(String name) {
        ((MinecraftServerExtension) wrapped).saveDynamicWorld(name);
    }

    @Override
    public void unloadWorld(String name) throws IOException {
        unloadWorld(name, true);
    }

    @Override
    public void unloadWorld(String name, boolean save) throws IOException {
        ((MinecraftServerExtension) wrapped).unloadDynamicWorld(name, save);
    }

    @Override
    public int runCommand(String command, int permissionLevel, boolean giveFeedback) {
        CommandSourceStack sourceStack = wrapped.createCommandSourceStack().withPermission(permissionLevel);
        if (!giveFeedback) sourceStack = sourceStack.withSuppressedOutput();
        return wrapped.getCommands().performCommand(sourceStack, command);
    }

    @Override
    public void sendMessage(Component message) {
        wrapped.sendMessage(ObjectMappings.convertComponent(message), Util.NIL_UUID);
    }

    @Override
    public int getPermissionLevel() {
        return 4;
    }

    @Override
    public MetisServer getServer() {
        return this;
    }
}
