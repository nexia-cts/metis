package com.combatreforged.metis.builder.mixin.server.players;

import com.combatreforged.metis.api.event.player.PlayerJoinEvent;
import com.combatreforged.metis.api.event.player.PlayerRespawnEvent;
import com.combatreforged.metis.api.event.server.ServerBroadcastMessageEvent;
import com.combatreforged.metis.api.world.util.Location;
import com.combatreforged.metis.builder.extension.server.MinecraftServerExtension;
import com.combatreforged.metis.builder.extension.server.SelectiveBorderChangeListener;
import com.combatreforged.metis.builder.implementation.Wrapped;
import com.combatreforged.metis.builder.implementation.WrappedMetisServer;
import com.combatreforged.metis.builder.implementation.util.ObjectMappings;
import com.combatreforged.metis.builder.implementation.world.WrappedWorld;
import com.combatreforged.metis.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.metis.builder.utils.PlayerData;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.UUID;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Shadow @Final private MinecraftServer server;
    /**
     *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a>, integrated into Metis from there <br>
     *  <h4>Licensed under MIT</h4> <br>
     *  Creates player data when they join the server
     */
    @Inject(at = @At("HEAD"), method = "placeNewPlayer")
    private void providePlayerData(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerData.addPlayerData(serverPlayer);
    }

    /**
     *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a>, integrated into Metis from there <br>
     *  <h4>Licensed under MIT</h4> <br>
     *  Clears player data when they exit the server
     */
    @Inject(at = @At("TAIL"), method = "remove")
    private void clearPlayerData(ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerData.removePlayerData(serverPlayer);
    }
    // BEGIN : CHAT EVENT
    /*
    @Inject(at = @At("HEAD"), method = "broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V", cancellable = true)
    public void callBroadcastMessageEvent(Component component, ChatType chatType, UUID uUID, CallbackInfo ci) {
    }

     */
    // END : CHAT EVENT

    // BEGIN: JOIN EVENT

    @ModifyArgs(method = "broadcastMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundChatPacket;<init>(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    private void handleChat(Args args) {
        MinecraftServer server = ((PlayerListAccessor) this).getServer();

        Component component = args.get(0);
        String key = ((TranslatableComponent) component).getKey();
        ChatType chatType = args.get(1);

        ServerBroadcastMessageEvent broadcastMessageEvent = new ServerBroadcastMessageEvent(Wrapped.wrap(server, WrappedMetisServer.class), ObjectMappings.convertComponent(component), ObjectMappings.convertChatType(chatType));
        ServerBroadcastMessageEvent.BACKEND.invoke(broadcastMessageEvent);

        if (!broadcastMessageEvent.isCancelled() && broadcastMessageEvent.getMessage() != null) {
            args.set(0, ObjectMappings.convertComponent(broadcastMessageEvent.getMessage()));
            return;
        }

        if (key.contains("multiplayer.player.left")) {
        }

        System.out.println("heya " + this.joinEvent.getJoinMessage() == null);
        System.out.println(key);
        if (key.contains("multiplayer.player.join") && this.joinEvent.getJoinMessage() != null) {
            args.set(0, ObjectMappings.convertComponent(joinEvent.getJoinMessage()));
            System.out.println("giggity giggity");
        }
    }

    @Unique Component joinMessage = null;
    @Unique PlayerJoinEvent joinEvent;

    /*
    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V", ordinal = 0))
    public void catchJoinMessage(PlayerList playerList, Component component, ChatType chatType, UUID uUID) {
        joinMessage = component;
        playerList.broadcastMessage(component, ChatType.SYSTEM, Util.NIL_UUID);
    }

     */

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void callPlayerJoinEvent(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerJoinEvent joinEvent = new PlayerJoinEvent(Wrapped.wrap(serverPlayer, WrappedPlayer.class),
                ObjectMappings.convertComponent(joinMessage));
        PlayerJoinEvent.BACKEND.invoke(joinEvent);

        this.joinEvent = joinEvent;

        PlayerJoinEvent.BACKEND.invokeEndFunctions(joinEvent);
    }
    // END: JOIN EVENT

    // BEGIN: PlayerRespawnEvent
    @Inject(method = "respawn", at = @At("HEAD"))
    public void setRespawnPosition(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir) {
        serverPlayer.setRespawnPosition(null, null, 0, false, false);
    }

    @Unique private PlayerRespawnEvent respawnEvent;
    @Inject(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    //, BlockPos blockPos, float f, boolean bl2, BlockPos blockPos, float f, boolean bl2,
    public void injectRespawnEvent(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir, BlockPos blockPos, float f, boolean bl2, ServerLevel serverLevel) {
        Location respawnPoint = blockPos != null ? new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), f, 0, Wrapped.wrap(serverLevel, WrappedWorld.class)) : null;
        GameType gameType = serverPlayer.gameMode.getGameModeForPlayer();
        this.respawnEvent = new PlayerRespawnEvent(Wrapped.wrap(serverPlayer, WrappedPlayer.class), respawnPoint, bl2, ObjectMappings.GAME_MODES.inverse().get(gameType));
        PlayerRespawnEvent.BACKEND.invoke(respawnEvent);
    }

    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", ordinal = 0, shift = At.Shift.AFTER))
    public BlockPos modifyBlockPos(BlockPos prev) {
        if (respawnEvent != null) {
            Location respawnLoc = respawnEvent.getSpawnpoint();
            return respawnLoc != null ? new BlockPos(respawnLoc.getX(), respawnLoc.getY(), respawnLoc.getZ()) : null;
        } else {
            return prev;
        }
    }

    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", ordinal = 0, shift = At.Shift.AFTER))
    public float modifyRespawnAngle(float prev) {
        if (respawnEvent != null) {
            return respawnEvent.getSpawnpoint() != null ? respawnEvent.getSpawnpoint().getYaw() : 0.0f;
        } else {
            return prev;
        }
    }

    /*
    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", ordinal = 0, shift = At.Shift.AFTER), ordinal = 1)
    public boolean modifyIsForced(boolean prev) {
        if (respawnEvent != null) {
            return respawnEvent.isSpawnpointForced();
        } else {
            return prev;
        }
    }
    */

    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", ordinal = 0, shift = At.Shift.AFTER))
    public ServerLevel modifyLevel(ServerLevel prev) {
        if (respawnEvent != null) {
            return respawnEvent.getSpawnpoint() != null ? ((WrappedWorld) respawnEvent.getSpawnpoint().getWorld()).unwrap() : null;
        } else {
            return prev;
        }
    }

    @Inject(method = "respawn", at = @At(value = "NEW", target = "net/minecraft/server/level/ServerPlayer", shift = At.Shift.BEFORE))
    public void modifyGameMode(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir) {
        if (respawnEvent != null) {
            serverPlayer.setGameMode(ObjectMappings.GAME_MODES.get(respawnEvent.getRespawnMode()));
        }
    }

    @Inject(method = "respawn", at = @At("TAIL"))
    public void nullifyRespawnEvent(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir) {
        if (respawnEvent != null) {
            respawnEvent.getPlayer().setGameMode(respawnEvent.getRespawnMode());
            PlayerRespawnEvent.BACKEND.invokeEndFunctions(respawnEvent);
            this.respawnEvent = null;
        }
    }
    // END: PlayerRespawnEvent

    @Redirect(method = "sendLevelInfo", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    public ServerLevel modifyServerLevel(MinecraftServer instance, ServerPlayer serverPlayer, ServerLevel serverLevel) {
        return ((MinecraftServerExtension) instance).getOverworldForLevel(serverLevel);
    }

    @Redirect(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/border/WorldBorder;addListener(Lnet/minecraft/world/level/border/BorderChangeListener;)V"))
    public void changeBorderChangeListener(WorldBorder border, BorderChangeListener prev, ServerLevel level) {
        border.addListener(new SelectiveBorderChangeListener(((MinecraftServerExtension) this.server).getRelatedLevels(level)));
    }
}
