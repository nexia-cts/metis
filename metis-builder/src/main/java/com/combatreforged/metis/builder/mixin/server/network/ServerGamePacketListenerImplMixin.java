package com.combatreforged.metis.builder.mixin.server.network;

import com.combatreforged.metis.api.MetisAPI;
import com.combatreforged.metis.api.command.CommandSourceInfo;
import com.combatreforged.metis.api.event.player.*;
import com.combatreforged.metis.api.world.entity.player.Player;
import com.combatreforged.metis.api.world.item.ItemStack;
import com.combatreforged.metis.api.world.util.Location;
import com.combatreforged.metis.builder.extension.server.level.ServerPlayerExtension;
import com.combatreforged.metis.builder.implementation.Wrapped;
import com.combatreforged.metis.builder.implementation.util.ObjectMappings;
import com.combatreforged.metis.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.metis.builder.implementation.world.item.WrappedItemStack;
import com.combatreforged.metis.builder.implementation.world.item.container.menu.WrappedContainerMenu;
import com.combatreforged.metis.builder.utils.CombatUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("InvalidBlockTag")
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;
    @Unique Entity targetEntity;

    @Shadow @SuppressWarnings("unused")
    private static boolean containsInvalidValues(ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
        return false;
    }

    @Shadow public abstract void teleport(double d, double e, double f, float g, float h);

    @Shadow @Final private MinecraftServer server;

    @Shadow public abstract void send(Packet<?> packet);

    @Shadow @Final public Connection connection;
    // BEGIN: DISCONNECT EVENT
    @Unique private PlayerDisconnectEvent disconnectEvent;
    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void injectDisconnectReason(Component component, CallbackInfo ci) {
        this.disconnectEvent = new PlayerDisconnectEvent(Wrapped.wrap(this.player, WrappedPlayer.class),
                component.getString(),
                null);
    }

    @Redirect(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    public void callPlayerDisconnectEvent(PlayerList playerList, Component component, ChatType chatType, UUID uUID, Component component2) {
        this.disconnectEvent.setLeaveMessage(ObjectMappings.convertComponent(component2));
        PlayerDisconnectEvent.BACKEND.invoke(disconnectEvent);

        if (disconnectEvent.getLeaveMessage() != null) {
            playerList.broadcastMessage(component, ChatType.SYSTEM, Util.NIL_UUID);
        }
    }

    @Inject(method = "onDisconnect", at = @At("TAIL"))
    public void nullifyDisconnectEvent(Component component, CallbackInfo ci) {
        PlayerDisconnectEvent.BACKEND.invokeEndFunctions(this.disconnectEvent);
        this.server.getPlayerList().getPlayers().forEach(players -> ((ServerPlayerExtension) players).showInTabList(this.player, true, false));
        this.disconnectEvent = null;
    }
    // END: DISCONNECT EVENT

    // BEGIN: MOVE EVENT
    @Unique private PlayerMoveEvent moveEvent;
    boolean inject = true;
    Location oldLocation;
    @Inject(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER))
    public void injectPlayerMoveEvent(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (!containsInvalidValues(packet) && inject) {
            Player player = Wrapped.wrap(this.player, WrappedPlayer.class);
            if (oldLocation == null) oldLocation = player.getLocation().copy();
            Location newLocation = new Location(
                    packet.getX(oldLocation.getX()),
                    packet.getY(oldLocation.getY()),
                    packet.getZ(oldLocation.getZ()),
                    packet.getYRot(oldLocation.getYaw()),
                    packet.getXRot(oldLocation.getPitch()),
                    player.getWorld());

            double[] movDif = getMovDif(oldLocation, newLocation);
            boolean hasDif = false;
            for (double d : movDif) {
                if (d >= 0.001) {
                    hasDif = true;
                    break;
                }
            }
            if (!hasDif)
                return;

            oldLocation = player.getLocation().copy();

            this.moveEvent = new PlayerMoveEvent(player, oldLocation, newLocation.copy(), false);
            PlayerMoveEvent.BACKEND.invoke(moveEvent);

            if (moveEvent.isCancelled()) {
                inject = false;
                player.teleport(oldLocation);
            }
            else if (!moveEvent.getNewPosition().equals(newLocation)) {
                inject = false;
                Location eventLocation = moveEvent.getNewPosition();
                this.teleport(eventLocation.getX(), eventLocation.getY(), eventLocation.getZ(), eventLocation.getYaw(), eventLocation.getPitch());
            }
        }
        else {
            inject = true;
        }
    }

    @Inject(method = "handleMovePlayer", at = @At("TAIL"))
    public void nullifyMoveEvent(ServerboundMovePlayerPacket serverboundMovePlayerPacket, CallbackInfo ci) {
        if (inject && moveEvent != null) {
            PlayerMoveEvent.BACKEND.invokeEndFunctions(moveEvent);
            this.moveEvent = null;
        }
    }
    
    private static double[] getMovDif(Location first, Location second) {
        return new double[] {
                Math.abs(second.getX() - first.getX()),
                Math.abs(second.getY() - first.getY()),
                Math.abs(second.getZ() - first.getZ()),
                Math.abs(second.getYaw() - first.getYaw()),
                Math.abs(second.getPitch() - first.getPitch())
        };
    }

    @Inject(method = "handlePlayerAction", at = @At("RETURN"), cancellable = true)
    public void injectSwapHandItemsEvent(ServerboundPlayerActionPacket serverboundPlayerActionPacket, CallbackInfo ci) {
        if (serverboundPlayerActionPacket.getAction() == ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND && !this.player.isSpectator()) {
            Player player = Wrapped.wrap(this.player, WrappedPlayer.class);
            ItemStack newOffhandItem = Wrapped.wrap(this.player.getItemInHand(InteractionHand.MAIN_HAND), WrappedItemStack.class);
            ItemStack oldOffhandItem = Wrapped.wrap(this.player.getItemInHand(InteractionHand.OFF_HAND), WrappedItemStack.class);
            PlayerSwapHandItemsEvent playerSwapHandItemsEvent = new PlayerSwapHandItemsEvent(player, oldOffhandItem, newOffhandItem);
            PlayerSwapHandItemsEvent.BACKEND.invoke(playerSwapHandItemsEvent);

            net.minecraft.world.item.ItemStack itemStack = this.player.getItemInHand(InteractionHand.OFF_HAND);
            if (!playerSwapHandItemsEvent.isCancelled()) {
                this.player.setItemInHand(InteractionHand.OFF_HAND, this.player.getItemInHand(InteractionHand.MAIN_HAND));
                this.player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                this.player.stopUsingItem();
            }

            ci.cancel();
        }
    }

    // END: MOVE EVENT

    @Redirect(method = "*", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/world/entity/player/Abilities;flying:Z"))
    public void injectChangeMovementStateEvent(Abilities abilities, boolean value) {
        if (abilities == this.player.abilities && value != abilities.flying) {
            Player player = Wrapped.wrap(this.player, WrappedPlayer.class);
            PlayerChangeMovementStateEvent event = new PlayerChangeMovementStateEvent(player, PlayerChangeMovementStateEvent.ChangedState.FLYING, value);
            PlayerChangeMovementStateEvent.BACKEND.invoke(event);

            boolean update = event.isCancelled() || event.getChangedValue() != value;
            this.player.abilities.flying = event.isCancelled() ? this.player.abilities.flying : event.getChangedValue();

            if (update) {
                this.player.connection.send(new ClientboundPlayerAbilitiesPacket(this.player.abilities));
            }

            PlayerChangeMovementStateEvent.BACKEND.invokeEndFunctions(event);
        }
    }

    @Unique private PlayerSwitchActiveSlotEvent switchActiveSlotEvent;
    @Inject(method = "handleSetCarriedItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER), cancellable = true)
    public void injectSwitchActiveSlotEvent(ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket, CallbackInfo ci) {
        if (serverboundSetCarriedItemPacket.getSlot() >= 0 && serverboundSetCarriedItemPacket.getSlot() < Inventory.getSelectionSize()) {
            this.switchActiveSlotEvent = new PlayerSwitchActiveSlotEvent(Wrapped.wrap(player, WrappedPlayer.class), serverboundSetCarriedItemPacket.getSlot());
            PlayerSwitchActiveSlotEvent.BACKEND.invoke(switchActiveSlotEvent);

            if (switchActiveSlotEvent.isCancelled()) {
                player.connection.send(new ClientboundSetCarriedItemPacket(this.player.inventory.selected));
                ci.cancel();
            }
        }
    }

    @Redirect(method = "handleSetCarriedItem", slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER),
            to = @At("TAIL")
    ), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundSetCarriedItemPacket;getSlot()I"))
    public int changeCarriedItem(ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket) {
        return switchActiveSlotEvent != null ? switchActiveSlotEvent.getNewSlot() : serverboundSetCarriedItemPacket.getSlot();
    }

    @Inject(method = "handleSetCarriedItem", at = @At("TAIL"))
    public void nullifySwitchActiveSlotEvent(ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket, CallbackInfo ci) {
        if (switchActiveSlotEvent != null) {
            if (player.inventory.selected != serverboundSetCarriedItemPacket.getSlot()) {
                player.connection.send(new ClientboundSetCarriedItemPacket(switchActiveSlotEvent.getNewSlot()));
            }
            PlayerSwitchActiveSlotEvent.BACKEND.invokeEndFunctions(switchActiveSlotEvent);
            switchActiveSlotEvent = null;
        }
    }

    /**
     * Send API command suggestions
     * @author rizecookey
     * @reason The automatic refmap creation doesn't handle lambda targets correctly, so we'll have to overwrite
     */
    @Overwrite @SuppressWarnings("FutureReturnValueIgnored")
    public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket serverboundCommandSuggestionPacket) {
        PacketUtils.ensureRunningOnSameThread(serverboundCommandSuggestionPacket, (ServerGamePacketListenerImpl) (Object) this, this.player.getLevel());

        CommandDispatcher<CommandSourceInfo> apiDispatcher = MetisAPI.getInstance().getServer().getCommandDispatcher();
        StringReader apiReader = new StringReader(serverboundCommandSuggestionPacket.getCommand());
        if (apiReader.canRead() && apiReader.peek() == '/') {
            apiReader.skip();
        }

        StringReader vanillaReader = new StringReader(serverboundCommandSuggestionPacket.getCommand());
        if (vanillaReader.canRead() && vanillaReader.peek() == '/') {
            vanillaReader.skip();
        }

        ParseResults<CommandSourceInfo> apiResults = apiDispatcher.parse(apiReader, Wrapped.wrap(this.player, WrappedPlayer.class).createCommandInfo());
        ParseResults<CommandSourceStack> vanillaResults = this.server.getCommands().getDispatcher().parse(vanillaReader, this.player.createCommandSourceStack());

        apiDispatcher.getCompletionSuggestions(apiResults)
                .thenCombine(this.server.getCommands().getDispatcher().getCompletionSuggestions(vanillaResults),
                        (apiSuggestions, vanillaSuggestions) -> !apiSuggestions.isEmpty() ? apiSuggestions : vanillaSuggestions)
                .thenAccept(suggestions -> this.player.connection.send(new ClientboundCommandSuggestionsPacket(serverboundCommandSuggestionPacket.getId(), suggestions)));
    }

    @Inject(method = "handleContainerClose", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER), cancellable = true)
    public void injectContainerCloseEvent(ServerboundContainerClosePacket serverboundContainerClosePacket, CallbackInfo ci) {
        PlayerCloseContainerEvent event = new PlayerCloseContainerEvent(Wrapped.wrap(this.player, WrappedPlayer.class), Wrapped.wrap(this.player.containerMenu, WrappedContainerMenu.class));
        PlayerCloseContainerEvent.BACKEND.invoke(event);

        if (event.isCancelled()) {
            this.send(new ClientboundOpenScreenPacket(this.player.containerMenu.containerId, this.player.containerMenu.getType(), ((ServerPlayerExtension) player).getLastContainerMenuTitle()));
            this.player.refreshContainer(this.player.containerMenu);
            ci.cancel();
        }

        PlayerCloseContainerEvent.BACKEND.invokeEndFunctions(event);
    }

    /**
     *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a>, integrated into Metis from there <br>
     *  <h4>Licensed under MIT</h4> <br>
     *  Stores the target for use later.
     */
    @Inject(method = "handleInteract", at = @At("HEAD"))
    private void handleInteract(ServerboundInteractPacket packet, CallbackInfo ci) {
        targetEntity = packet.getTarget(player.getLevel());
    }

    /**
     *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a>, integrated into Metis from there <br>
     *  <h4>Licensed under MIT</h4> <br>
     *  If return value is smaller than player reach the interaction will be a success, otherwise pass
     */
    @Redirect(method = "handleInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;distanceToSqr(Lnet/minecraft/world/phys/Vec3;)D"))
    private double getAttackDistance(Vec3 instance, Vec3 vec3) {
        // If target is not a player do vanilla code
        if (!(targetEntity instanceof ServerPlayer)) {
            Vec3 eyePosition = player.getEyePosition(0);
            return (eyePosition).distanceToSqr(targetEntity.getBoundingBox().getNearestPointTo(eyePosition));
        }

        ServerPlayer target = (ServerPlayer) targetEntity;
        if (CombatUtil.allowReach(player, target)) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Redirect(method = "handleInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getCurrentAttackReach(F)F"))
    public float modifyMax(ServerPlayer instance, float v) {
        return instance.getCurrentAttackReach(v) + 0.75F;
    }

    @Inject(method = "handleCommand", at = @At("HEAD"), cancellable = true)
    public void handleCommand(String string, CallbackInfo ci) {
        try {
            this.player.connection.send(new ClientboundCommandSuggestionsPacket(15, Suggestions.empty().get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        PlayerCommandSendEvent event = new PlayerCommandSendEvent(Wrapped.wrap(this.player, WrappedPlayer.class), string);
        PlayerCommandSendEvent.BACKEND.invoke(event);

        if (event.isCancelled()) {
            ci.cancel();
        } else {
            this.server.getCommands().performCommand(this.player.createCommandSourceStack(), event.getCommand());
            ci.cancel();
        }
    }
}
