package com.combatreforged.metis.api.command;

import com.combatreforged.metis.api.MetisServer;
import net.kyori.adventure.text.Component;

public interface CommandSender {
    default int runCommand(String command) {
        return runCommand(command, getPermissionLevel(), true);
    }

    default int runCommand(String command, int permissionLevel) {
        return runCommand(command, permissionLevel, true);
    }

    int runCommand(String command, int permissionLevel, boolean giveFeedback);

    void sendMessage(Component message);

    int getPermissionLevel();

    MetisServer getServer();
}
