package com.combatreforged.metis.builder.mixin.commands;

import com.combatreforged.metis.api.MetisAPI;
import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.command.CommandSender;
import com.combatreforged.metis.api.command.CommandSourceInfo;
import com.combatreforged.metis.api.event.player.PlayerCommandRegisterEvent;
import com.combatreforged.metis.api.world.World;
import com.combatreforged.metis.api.world.command.CommandType;
import com.combatreforged.metis.api.world.entity.Entity;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.util.Location;
import com.combatreforged.metis.builder.implementation.Wrapped;
import com.combatreforged.metis.builder.implementation.WrappedMetisServer;
import com.combatreforged.metis.builder.implementation.world.WrappedWorld;
import com.combatreforged.metis.builder.implementation.world.entity.WrappedEntity;
import com.combatreforged.metis.builder.implementation.world.entity.player.WrappedPlayer;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.*;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(Commands.class)
public abstract class CommandsMixin {
    @Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;

    /*
    // Makes non-syntax errors log
    @Redirect(method = "performCommand", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;isDebugEnabled()Z",remap = false))
    public boolean activateCommandExecutionErrors(Logger instance) {
        return true;
    }

     */

    @Redirect(method = "performCommand", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;execute(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)I", remap = false))
    public int enableAPICommands(CommandDispatcher<?> commandDispatcher, StringReader input, Object source, CommandSourceStack commandSourceStack, String string) throws CommandSyntaxException {
        if (commandDispatcher == this.dispatcher) {
            CommandDispatcher<CommandSourceInfo> apiDispatcher = MetisAPI.getInstance().getServer().getCommandDispatcher();
            CommandSource source1 = ((CommandSourceStackAccessor) commandSourceStack).getSource();
            CommandSender sender = source1 instanceof MinecraftServer ?
                    Wrapped.wrap(source1, WrappedMetisServer.class)
                    : Wrapped.wrap(source1, WrappedEntity.class);
            Entity entity = Wrapped.wrap(commandSourceStack.getEntity(), WrappedEntity.class);
            MetisServer server = Wrapped.wrap(commandSourceStack.getServer(), WrappedMetisServer.class);
            Vec3 vec3 = commandSourceStack.getPosition();
            Vec2 vec2 = commandSourceStack.getRotation();
            World world = Wrapped.wrap(commandSourceStack.getLevel(), WrappedWorld.class);
            Location loc = new Location(vec3.x, vec3.y, vec3.z, vec2.y, vec2.x, world);
            CommandSourceInfo info = CommandSourceInfo.builder()
                    .source(sender)
                    .executingEntity(entity)
                    .server(server)
                    .location(loc)
                    .build();
            try {
                return apiDispatcher.execute(input, info);
            } catch (CommandSyntaxException e) {
                return this.dispatcher.execute(input, commandSourceStack);
            }
        } else {
            return dispatcher.execute(input, (CommandSourceStack) source);
        }
    }

    /*
    @Inject(method = "sendCommands", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/Commands;fillUsableCommands(Lcom/mojang/brigadier/tree/CommandNode;Lcom/mojang/brigadier/tree/CommandNode;Lnet/minecraft/commands/CommandSourceStack;Ljava/util/Map;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void injectAPICommands(ServerPlayer serverPlayer, CallbackInfo ci, Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> map, RootCommandNode<SharedSuggestionProvider> rootCommandNode) {
        CommandDispatcher<CommandSourceInfo> apiDispatcher = MetisAPI.getInstance().getServer().getCommandDispatcher();

        Map<CommandNode<CommandSourceInfo>, CommandNode<SharedSuggestionProvider>> apiMap = new HashMap<>();
        apiMap.put(apiDispatcher.getRoot(), rootCommandNode);

        Player player = Wrapped.wrap(serverPlayer, WrappedPlayer.class);
        this.fillAPICommands(apiDispatcher.getRoot(), rootCommandNode, player.createCommandInfo(), apiMap);
    }
     */

    @Inject(method = "sendCommands", at = @At("HEAD"), cancellable = true)
    public void sendCommandsInject(ServerPlayer serverPlayer, CallbackInfo ci) {
        CommandDispatcher<CommandSourceStack> myDispatcher = new CommandDispatcher<>();

        Player player = Wrapped.wrap(serverPlayer, WrappedPlayer.class);
        PlayerCommandRegisterEvent registerEvent = new PlayerCommandRegisterEvent(player);
        PlayerCommandRegisterEvent.BACKEND.invoke(registerEvent);

        if (!registerEvent.getBlockedCommands().contains(CommandType.ADVANCEMENT))
            AdvancementCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.ATTRIBUTE))
            AttributeCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.EXECUTE))
            ExecuteCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.BOSS_BAR))
            BossBarCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.CLEAR_INVENTORY))
            ClearInventoryCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.CLONE))
            CloneCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.DATA))
            DataCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.DATA_PACK))
            DataPackCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.DEBUG))
            DebugCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.DEFAULT_GAMEMODE))
            DefaultGameModeCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.DIFFICULTY))
            DifficultyCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.EFFECT))
            EffectCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.EMOTE))
            EmoteCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.ENCHANT))
            EnchantCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.EXPERIENCE))
            ExperienceCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.FILL))
            FillCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.FORCE_LOAD))
            ForceLoadCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.FUNCTION))
            FunctionCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.GAMEMODE))
            GameModeCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.GAMERULE))
            GameRuleCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.GIVE))
            GiveCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.HELP))
            HelpCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.KICK))
            KickCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.KILL))
            KillCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.LIST))
            ListPlayersCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.LOCATE))
            LocateCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.LOCATE_BIOME))
            LocateBiomeCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.LOOT))
            LootCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.MSG))
            MsgCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.PARTICLE))
            ParticleCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.PLAY_SOUND))
            PlaySoundCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.RELOAD))
            ReloadCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.RECIPE))
            RecipeCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.REPLACE_ITEM))
            ReplaceItemCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SAY))
            SayCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SCHEDULE))
            ScheduleCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SCOREBOARD))
            ScoreboardCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SET_BLOCK))
            SetBlockCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SET_SPAWN))
            SetSpawnCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SET_WORLD_SPAWN))
            SetWorldSpawnCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SPECTATE))
            SpectateCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SPREAD_PLAYERS))
            SpreadPlayersCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.STOP_SOUND))
            StopSoundCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SUMMON))
            SummonCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.TAG))
            TagCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.TEAM))
            TeamCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.TEAM_MSG))
            TeamMsgCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.TELEPORT))
            TeleportCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.TELL_RAW))
            TellRawCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.TIME))
            TimeCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.TITLE))
            TitleCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.TRIGGER))
            TriggerCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.WEATHER))
            WeatherCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.WORLD_BORDER))
            WorldBorderCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.BAN_IP))
            BanIpCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.BAN_LIST))
            BanListCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.BAN_PLAYER))
            BanPlayerCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.DE_OP))
            DeOpCommands.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.OP))
            OpCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.PARDON))
            PardonCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.PARDON_IP))
            PardonIpCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SAVE_ALL))
            SaveAllCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SAVE_OFF))
            SaveOffCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SAVE_ON))
            SaveOnCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.SET_PLAYER_IDLE_TIMEOUT))
            SetPlayerIdleTimeoutCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.STOP))
            StopCommand.register(myDispatcher);
        if (!registerEvent.getBlockedCommands().contains(CommandType.WHITELIST))
            WhitelistCommand.register(myDispatcher);

        Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> map = Maps.newHashMap();
        RootCommandNode<SharedSuggestionProvider> rootCommandNode = new RootCommandNode<>();
        map.put(myDispatcher.getRoot(), rootCommandNode);
        this.fillUsableCommands(myDispatcher.getRoot(), rootCommandNode, serverPlayer.createCommandSourceStack(), map);

        CommandDispatcher<CommandSourceInfo> apiDispatcher = MetisAPI.getInstance().getServer().getCommandDispatcher();

        Map<CommandNode<CommandSourceInfo>, CommandNode<SharedSuggestionProvider>> apiMap = new HashMap<>();
        apiMap.put(apiDispatcher.getRoot(), rootCommandNode);

        this.fillAPICommands(registerEvent, apiDispatcher.getRoot(), rootCommandNode, player.createCommandInfo(), apiMap);

        serverPlayer.connection.send(new ClientboundCommandsPacket(rootCommandNode));

        ci.cancel();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void fillUsableCommands(CommandNode<CommandSourceStack> commandNode, CommandNode<SharedSuggestionProvider> commandNode2, CommandSourceStack commandSourceStack, Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> map) {
        for (CommandNode<CommandSourceStack> commandNode3 : commandNode.getChildren()) {
            if (commandNode3.canUse(commandSourceStack)) {
                ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder = (ArgumentBuilder) commandNode3.createBuilder();
                argumentBuilder.requires((sharedSuggestionProvider) -> true);
                if (argumentBuilder.getCommand() != null)
                    argumentBuilder.executes((commandContext) -> 0);

                if (argumentBuilder instanceof RequiredArgumentBuilder) {
                    RequiredArgumentBuilder<SharedSuggestionProvider, ?> requiredArgumentBuilder = (RequiredArgumentBuilder) argumentBuilder;
                    if (requiredArgumentBuilder.getSuggestionsProvider() != null)
                        requiredArgumentBuilder.suggests(SuggestionProviders.safelySwap(requiredArgumentBuilder.getSuggestionsProvider()));
                }

                if (argumentBuilder.getRedirect() != null)
                    argumentBuilder.redirect(map.get(argumentBuilder.getRedirect()));

                CommandNode<SharedSuggestionProvider> requiredArgumentBuilder = argumentBuilder.build();
                map.put(commandNode3, requiredArgumentBuilder);
                commandNode2.addChild(requiredArgumentBuilder);
                if (!commandNode3.getChildren().isEmpty())
                    this.fillUsableCommands(commandNode3, requiredArgumentBuilder, commandSourceStack, map);
            }
        }

    }

    // Copy of Commands.fillUsableCommands(...) for our dispatcher
    // Bit sketchy, but eh...
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void fillAPICommands(PlayerCommandRegisterEvent registerEvent, CommandNode<CommandSourceInfo> commandNode, CommandNode<SharedSuggestionProvider> commandNode2, CommandSourceInfo commandSourceStack, Map<CommandNode<CommandSourceInfo>, CommandNode<SharedSuggestionProvider>> map) {
        for (CommandNode<CommandSourceInfo> commandSourceInfoCommandNode : commandNode.getChildren()) {
            if (commandSourceInfoCommandNode.canUse(commandSourceStack)) {
                ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder = (ArgumentBuilder) commandSourceInfoCommandNode.createBuilder();
                argumentBuilder.requires((sharedSuggestionProvider) -> true);
                if (argumentBuilder.getCommand() != null)
                    argumentBuilder.executes((commandContext) -> 0);

                if (argumentBuilder instanceof RequiredArgumentBuilder) {
                    RequiredArgumentBuilder<SharedSuggestionProvider, ?> requiredArgumentBuilder = (RequiredArgumentBuilder) argumentBuilder;
                    if (requiredArgumentBuilder.getSuggestionsProvider() != null)
                        requiredArgumentBuilder.suggests(SuggestionProviders.safelySwap(requiredArgumentBuilder.getSuggestionsProvider()));
                }

                if (argumentBuilder.getRedirect() != null)
                    argumentBuilder.redirect(map.get(argumentBuilder.getRedirect()));


                CommandNode<SharedSuggestionProvider> commandNode4 = argumentBuilder.build();

                if (!registerEvent.getBlockedCustomCommands().contains(commandNode4.getName())) {
                    map.put(commandSourceInfoCommandNode, commandNode4);
                    commandNode2.addChild(commandNode4);
                }
                if (!commandSourceInfoCommandNode.getChildren().isEmpty())
                    this.fillAPICommands(registerEvent, commandSourceInfoCommandNode, commandNode4, commandSourceStack, map);
            }
        }

    }
}
