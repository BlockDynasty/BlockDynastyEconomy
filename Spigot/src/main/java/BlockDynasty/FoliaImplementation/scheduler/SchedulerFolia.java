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

package BlockDynasty.FoliaImplementation.scheduler;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import abstractions.platform.scheduler.ContextualTask;
import abstractions.platform.scheduler.IScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class SchedulerFolia implements IScheduler {
    public static BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();

    public void runLater(long delay, ContextualTask contextualTask) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, task -> contextualTask.getRunnable().run(), delay);
    }

    public void runAsync(ContextualTask contextualTask) {
        Bukkit.getAsyncScheduler().runNow(plugin, scheduledTask -> contextualTask.getRunnable().run());
    }

    public void run(ContextualTask contextualTask){
        if (contextualTask.hasEntityContext()) {
            Entity entity = (Entity) contextualTask.getEntityContext().getRoot();
            entity.getScheduler().execute(plugin, contextualTask.getRunnable(), null, 1);
            //Bukkit.getRegionScheduler().execute(plugin, contextualTask.getEntityContext().getLocation(), contextualTask.getRunnable());
            return;
        }

        if(contextualTask.hasLocationContext()) {
            Location location = (Location) contextualTask.getLocationContext().getRoot();
            Bukkit.getRegionScheduler().execute(plugin, location, contextualTask.getRunnable());
            return;
        }

        Bukkit.getGlobalRegionScheduler().execute(plugin, contextualTask.getRunnable());
    }

    public static SchedulerFolia init() {
        return new SchedulerFolia();
    }
}
