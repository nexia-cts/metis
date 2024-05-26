package com.combatreforged.metis.builder.implementation.command;

import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.command.CommandSender;
import com.combatreforged.metis.api.command.CommandSourceInfo;
import com.combatreforged.metis.api.world.entity.Entity;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.scoreboard.ScoreboardTeam;
import com.combatreforged.metis.api.world.util.Location;
import com.combatreforged.metis.builder.implementation.WrappedMetisServer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandSourceInfoImpl implements CommandSourceInfo, SharedSuggestionProvider {
    private final CommandSender sender;
    @Nullable
    private final Entity executingEntity;
    private final Location location;
    private final MetisServer server;

    public CommandSourceInfoImpl(CommandSender sender, @Nullable Entity executingEntity, Location location, MetisServer server) {
        this.sender = sender;
        this.executingEntity = executingEntity;
        this.location = location;
        this.server = server;
    }

    @Override
    public CommandSender getSender() {
        return sender;
    }

    @Override
    public @Nullable Entity getExecutingEntity() {
        return executingEntity;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public MetisServer getServer() {
        return server;
    }

    @Override
    public void sendMessage(Component message) {
        sender.sendMessage(message);
    }

    @Override
    public Collection<String> getOnlinePlayerNames() {
        return this.server.getPlayers().stream()
                .map(Player::getRawName)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<String> getAllTeams() {
        return this.server.getServerScoreboard().getAllTeams().stream()
                .map(ScoreboardTeam::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ResourceLocation> getAvailableSoundEvents() {
        return Registry.SOUND_EVENT.keySet();
    }

    @Override
    public Stream<ResourceLocation> getRecipeNames() {
        return ((WrappedMetisServer) this.server).unwrap().getRecipeManager().getRecipeIds();
    }

    @Override
    public CompletableFuture<Suggestions> customSuggestion(CommandContext<SharedSuggestionProvider> commandContext, SuggestionsBuilder suggestionsBuilder) {
        return null;
    }

    @Override
    public Set<ResourceKey<Level>> levels() {
        return ((WrappedMetisServer) this.server).unwrap().levelKeys();
    }

    @Override
    public RegistryAccess registryAccess() {
        return ((WrappedMetisServer) this.server).unwrap().registryAccess();
    }

    @Override
    public boolean hasPermission(int i) {
        return this.sender.getPermissionLevel() >= i;
    }
}
