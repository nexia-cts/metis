package com.combatreforged.metis.api.entrypoint.interfaces;

import net.kyori.adventure.text.Component;

public interface MessageReceiver {
    void sendMessage(Component component);
    void sendMessage(Component component, Type type);

    enum Type {
        CHAT, SYSTEM, ACTION_BAR
    }
}
