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

package abstractions.platform.scheduler;

public class ContextualTask {
    private final Runnable runnable;
    private final IEntity entity;
    private final ILocation locationContext;

    //future use cases could include for folia total compatibility
    //private final World worldContext;  // Contextual world, if needed

    private ContextualTask(Runnable runnable, IEntity entity, ILocation locationContext) {
        this.runnable = runnable;
        this.entity = entity;
        this.locationContext = locationContext;
    }

    // Static factory methods
    public static ContextualTask build(Runnable runnable) {
        return new ContextualTask(runnable, null, null );
    }

    public static ContextualTask build(Runnable runnable, IEntity entity) {
        return new ContextualTask(runnable, entity, null);
    }

    public static ContextualTask build(Runnable runnable, ILocation locationContext) {
        return new ContextualTask(runnable, null, locationContext);
    }

    public static ContextualTask build(Runnable runnable, IEntity entity, ILocation locationContext) {
        return new ContextualTask(runnable, entity, locationContext);
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public IEntity getEntityContext() {
        return entity;
    }

    public ILocation getLocationContext() {
        return locationContext;
    }

    public boolean hasEntityContext() {
        return entity != null;
    }

    public boolean hasLocationContext() {
        return locationContext != null;
    }
}