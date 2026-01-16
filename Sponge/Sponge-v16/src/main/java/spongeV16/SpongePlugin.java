/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spongeV16;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.lifecycle.*;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import spongeV13.SpongePluginCommon;
import spongeV13.adapters.integrations.spongeEconomyApi.EconomyServiceAdapter;

import java.nio.file.Path;

@Plugin("blockdynastyeconomy")
public class SpongePlugin extends SpongePluginCommon {

    @Inject
    public SpongePlugin(final PluginContainer container, final Logger logger,@ConfigDir(sharedRoot = false) final Path configDir) {
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
    public void registerEconomyServiceV16(ProvideServiceEvent.EngineScoped<EconomyService,Server> event) {
       event.suggest(EconomyServiceAdapter::new);
    }

    @Listener
    public void onServerStopping(final StoppingEngineEvent<Server> event) {
        super.onServerStopping(event);
    }

    @Listener
    public void onRegisterData(RegisterDataEvent event) {
        super.onRegisterData(event);
    }

    @Listener
    public void onBlockPlace(InteractBlockEvent.Secondary event){
        super.onBlockPlace(event);
    }
}
