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

package BlockDynasty.BukkitImplementation.scheduler;

import abstractions.platform.scheduler.ContextualTask;

public class Scheduler {

    /**
     * Runs a task on another thread after a delay.
     * @param delay - Delay in ticks.
     * @param contextualTask - Context to perform.
     */
    public static void runLater(long delay, ContextualTask contextualTask) {
        SchedulerFactory.runLater( delay, contextualTask);
    }

    /**
     * Runs a task on another thread immediately.
     * @param contextualTask - Context to perform.
     */
    public static void runAsync(ContextualTask contextualTask) {
        SchedulerFactory.runAsync( contextualTask);
    }

    /**
     * Runs a task on the main thread immediately
     * @param contextualTask - Context to perform
     */
    public static  void run(ContextualTask contextualTask) {
        SchedulerFactory.run(contextualTask);
    }
}
