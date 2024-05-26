package com.combatreforged.metis.api.event.server;

import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.event.Cancellable;
import com.combatreforged.metis.api.event.EventBackend;
import com.combatreforged.metis.api.world.chat.ChatType;
import net.kyori.adventure.text.Component;


/**
 * Event triggered for server broadcast messages.
 */
public class ServerBroadcastMessageEvent extends ServerEvent implements Cancellable {
    public static final EventBackend<ServerBroadcastMessageEvent> BACKEND = EventBackend.create(ServerBroadcastMessageEvent.class);

    private boolean cancelled = false;

    private Component message;
    private final ChatType chatType;

    public ServerBroadcastMessageEvent(MetisServer server, Component message, ChatType chatType) {
        super(server);
        this.message = message;
        this.chatType = chatType;
    }

    /**
     * Set the message to broadcast.
     *
     * @param message New message to broadcast
     */
    public void setMessage(Component message) {
        this.message = message;
    }

    /** Get the message to broadcast. */
    public Component getMessage() {
        return message;
    }

    /** Get the type of message to broadcast. */
    public ChatType getChatType() {
        return chatType;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}
