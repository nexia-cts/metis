package com.combatreforged.metis.builder.mixin.server.dedicated;

import com.combatreforged.metis.api.MetisAPI;
import com.combatreforged.metis.api.MetisServer;
import com.combatreforged.metis.api.entrypoint.MetisPlugin;
import com.combatreforged.metis.api.event.server.ServerStartupFinishEvent;
import com.combatreforged.metis.builder.MetisBuilder;
import com.combatreforged.metis.builder.extension.server.MinecraftServerExtension;
import com.combatreforged.metis.builder.extension.wrap.Wrap;
import com.combatreforged.metis.builder.implementation.WrappedMetisServer;
import com.combatreforged.metis.builder.implementation.builder.BuilderImpl;
import com.combatreforged.metis.builder.implementation.util.ObjectMappings;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer implements Wrap<MetisServer>, MinecraftServerExtension {
    private WrappedMetisServer wrapped;
    private MetisAPI api;
    public DedicatedServerMixin(Thread thread, RegistryAccess.RegistryHolder registryHolder, LevelStorageSource.LevelStorageAccess levelStorageAccess, WorldData worldData, PackRepository packRepository, Proxy proxy, DataFixer dataFixer, ServerResources serverResources, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, GameProfileCache gameProfileCache, ChunkProgressListenerFactory chunkProgressListenerFactory) {
        super(thread, registryHolder, levelStorageAccess, worldData, packRepository, proxy, dataFixer, serverResources, minecraftSessionService, gameProfileRepository, gameProfileCache, chunkProgressListenerFactory);
    }

    @Inject(method = "initServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;loadLevel()V", shift = At.Shift.AFTER))
    public void loadAPI(CallbackInfoReturnable<Boolean> cir) {
        MetisBuilder.LOGGER.info("Injecting the API...");

        this.wrapped = new WrappedMetisServer((DedicatedServer) (Object) this);
        this.api = new MetisAPI(wrapped, new BuilderImpl((DedicatedServer) (Object) this, LogManager.getLogger("FactoryWrapBuilder")));
        ObjectMappings.initIndependent();

        MetisBuilder.LOGGER.info("Initializing plugins...");

        this.loadPlugins();

        MetisBuilder.LOGGER.info("Done.");
    }

    @Inject(method = "initServer", at = @At("TAIL"))
    public void callServerStartupFinishEvent(CallbackInfoReturnable<Boolean> cir) {
        ServerStartupFinishEvent event = new ServerStartupFinishEvent(this.wrapped);
        ServerStartupFinishEvent.BACKEND.invoke(event);
        ServerStartupFinishEvent.BACKEND.invokeEndFunctions(event);
    }

    public void loadPlugins() {
        List<EntrypointContainer<MetisPlugin>> entrypoints = FabricLoader.getInstance()
                .getEntrypointContainers("factory", MetisPlugin.class);
        StringBuilder sB = new StringBuilder().append("Found ").append(entrypoints.size()).append(" plugin entrypoints");
        for (int i = 0; i < entrypoints.size(); i++) {
            if (i == 0) sB.append(": \n");
            sB.append("    - ")
                    .append(entrypoints.get(i).getEntrypoint().getClass().getSimpleName())
                    .append(" [")
                    .append(entrypoints.get(i).getProvider().getMetadata().getId())
                    .append("@")
                    .append(entrypoints.get(i).getProvider().getMetadata().getVersion())
                    .append("]");
            if (i < (entrypoints.size() - 1)) sB.append(", \n");
        }
        MetisBuilder.LOGGER.info(sB.toString());

        List<ModContainer> loaded = new ArrayList<>();
        Map<ModContainer, List<MetisPlugin>> modEntrypoints = new HashMap<>();
        for (EntrypointContainer<MetisPlugin> container : entrypoints) {
            if (modEntrypoints.containsKey(container.getProvider())) {
                modEntrypoints.get(container.getProvider()).add(container.getEntrypoint());
            } else {
                List<MetisPlugin> list = new ArrayList<>();
                list.add(container.getEntrypoint());
                modEntrypoints.put(container.getProvider(), list);
            }
        }

        modEntrypoints.keySet().forEach(mod -> this.loadEntrypoints(mod, modEntrypoints, loaded));
    }

    public void loadEntrypoints(ModContainer modContainer, Map<ModContainer, List<MetisPlugin>> entrypointMap, List<ModContainer> loaded) {
        for (ModDependency dependencyContainer : modContainer.getMetadata().getDepends()) {
            ModContainer dependency = FabricLoader.getInstance().getModContainer(dependencyContainer.getModId()).orElseThrow(() -> new IllegalStateException("Dependency not present"));
            if (entrypointMap.containsKey(dependency)) {
                this.loadEntrypoints(dependency, entrypointMap, loaded);
            }
        }
        if (!loaded.contains(modContainer)) {
            entrypointMap.get(modContainer).forEach(entrypoint -> entrypoint.onMetisLoad(this.api, this.wrapped));
            loaded.add(modContainer);
        }
    }

    @Override
    public MetisServer wrap() {
        return wrapped;
    }
}
