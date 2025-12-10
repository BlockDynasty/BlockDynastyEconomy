package spongeV13;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ProvideServiceEvent;
import org.spongepowered.api.event.lifecycle.RegisterChannelEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.nio.file.Path;

@Plugin("blockdynastyeconomy")
public class SpongePlugin extends SpongePluginCommon{

    @Inject
    public SpongePlugin(final PluginContainer container, final Logger logger, @ConfigDir(sharedRoot = false) final Path configDir) {
        super(container, logger, configDir);
    }


    @Listener
    public void onEngineStarting(StartingEngineEvent<Server> event) {
        super.onEngineStarting(event);
    }

    @Listener
    public void onRegisterChannel(final RegisterChannelEvent event) {
        super.onRegisterChannel(event);
    }

    @Listener
    public void registerEconomyServiceV16(ProvideServiceEvent.EngineScoped<EconomyService> event) {
        super.registerEconomyService(event);
    }

    @Listener
    public void onServerStopping(final StoppingEngineEvent<Server> event) {
        super.onServerStopping(event);
    }
}