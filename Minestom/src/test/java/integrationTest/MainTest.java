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

package integrationTest;

import net.blockdynasty.economy.api.DynastyEconomy;
import net.blockdynasty.economy.api.entity.Account;
import net.blockdynasty.economy.minestom.EconomySystem;
//import net.minestom.server.Auth;
import integrationTest.basicCommands.toggleOp;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;

import java.util.Optional;

//example main class to start a minestom server with economy system
public class MainTest {
    public static void main(String[] args) {
       //--------start basic server setup----------------------
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        instanceContainer.setTime(6000);
        instanceContainer.setChunkSupplier(LightingChunk::new);
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });
        minecraftServer.start("0.0.0.0", 25565);
        //--------fin basic server setup-------------------------

        //test
        MinecraftServer.getCommandManager().register(new toggleOp()); //test


        //--------start economy system setup----------------------
        EconomySystem.start(true); // <----------Initialize the economy system and this is ready to use!
        //EconomySystem.start(true, new PermsServiceDefault()); // <----------Initialize the economy system with custom permissions service and this is ready to use!



        //if you want you can get economy api and work!
        Optional<DynastyEconomy> optional = EconomySystem.getApi();
        if (optional.isPresent()) {
            DynastyEconomy economy = optional.get();
            Account account = economy.getAccount("Nullplague");
            if (account != null) {
                account.getBalances().forEach(m -> System.out.println( m.getAmount()+ " "+m.getCurrency().getSingular()));
            }
            //use economy api
        }
    }
}
