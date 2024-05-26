package com.combatreforged.metis.api.command;

import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.world.entity.Entity;
import com.combatreforged.metis.api.world.util.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface CommandSourceInfo {
    CommandSender getSender();

    @Nullable Entity getExecutingEntity();

    Location getLocation();

    MetisServer getServer();

    void sendMessage(Component message);

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private CommandSender source;
        @Nullable private Entity executingEntity;
        private Location location;
        private MetisServer server;

        Builder() {}

        public Builder source(CommandSender source) {
            this.source = source;
            return this;
        }

        public Builder executingEntity(Entity entity) {
            this.executingEntity = entity;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder server(MetisServer server) {
            this.server = server;
            return this;
        }

        public CommandSourceInfo build() {
            return com.combatreforged.metis.api.builder.Builder.getInstance().createCommandSourceInfo(source, executingEntity, location, server);
        }
    }
}
