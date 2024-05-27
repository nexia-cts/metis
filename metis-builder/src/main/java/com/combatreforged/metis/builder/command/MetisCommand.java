package com.combatreforged.metis.builder.command;

import com.combatreforged.metis.api.command.CommandSourceInfo;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.stream.Collectors;

import static com.combatreforged.metis.api.command.CommandUtils.literal;

public final class MetisCommand {
    private MetisCommand() {}

    public static void register(CommandDispatcher<CommandSourceInfo> dispatcher) {
        dispatcher.register(
                literal("metis")
                        .requires(info -> info.getSender().getPermissionLevel() >= 4)
                        .then(literal("version")
                                .executes(context -> {
                                    FabricLoader loader = FabricLoader.getInstance();
                                    assert loader.getModContainer("metis-api").isPresent() && loader.getModContainer("metis-builder").isPresent();
                                    String apiVersion = loader.getModContainer("metis-api").get().getMetadata().getVersion().toString();
                                    String builderVersion = loader.getModContainer("metis-builder").get().getMetadata().getVersion().toString();
                                    context.getSource().sendMessage(Component.text("metis version: \n ")
                                            .append(Component.text("- API: [metis-api@" + apiVersion + "]\n - Implementation: [metis-builder@" + builderVersion + "]")
                                                    .color(NamedTextColor.DARK_GREEN)));
                                    return 0;
                                }))
                        .then(literal("plugins")
                                .executes(context -> {
                                    List<ModMetadata> plugins = FabricLoader.getInstance().getAllMods()
                                            .stream()
                                            .map(ModContainer::getMetadata)
                                            .filter(metadata -> metadata.getDependencies().stream()
                                                    .anyMatch(dep -> dep.getModId().equals("metis-api")))
                                            .toList();
                                    Component result = Component.text("Found " + plugins.size() + " plugin(s): ");
                                    for (ModMetadata plugin : plugins) {
                                        result = result.append(Component.text("[" + plugin.getId() + "@" + plugin.getVersion().toString() + "]"));
                                        if (plugins.indexOf(plugin) != plugins.size() - 1) {
                                            result = result.append(Component.text(", "));
                                        }
                                    }
                                    context.getSource().sendMessage(result);
                                    return 0;
                                }))
        );
    }
}
