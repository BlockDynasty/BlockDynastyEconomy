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

package spongeV13.adapters.scheduler;

import spongeV13.SpongePluginCommon;
import lib.scheduler.ContextualTask;
import lib.scheduler.IScheduler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.time.Duration;

public class Scheduler implements IScheduler {

    @Override
    public void runLater(long delay, ContextualTask contextualTask) {
        Duration delayDuration = Duration.ofMillis(delay * 50);

        Sponge.server().scheduler().submit(
                Task.builder()
                        .plugin(SpongePluginCommon.getPlugin())
                        .delay(delayDuration)
                        .execute(contextualTask.getRunnable())
                        .build()
        );
    }

    @Override
    public void runAsync(ContextualTask contextualTask) {
        Sponge.asyncScheduler().submit(
                Task.builder()
                        .plugin(SpongePluginCommon.getPlugin())
                        .execute(contextualTask.getRunnable())
                        .build()
        );
    }

    @Override
    public void run(ContextualTask contextualTask) {
        Sponge.server().scheduler().submit(
                Task.builder()
                        .plugin(SpongePluginCommon.getPlugin())
                        .execute(contextualTask.getRunnable())
                        .build()
        );
    }
}
