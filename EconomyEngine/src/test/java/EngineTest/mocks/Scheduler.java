package EngineTest.mocks;

import abstractions.platform.scheduler.ContextualTask;
import abstractions.platform.scheduler.IScheduler;

public class Scheduler implements IScheduler {

    @Override
    public void runLater(long delay, ContextualTask contextualTask) {
        contextualTask.getRunnable().run();
    }

    @Override
    public void runAsync(ContextualTask contextualTask) {
        contextualTask.getRunnable().run();
    }

    @Override
    public void run(ContextualTask contextualTask) {
        contextualTask.getRunnable().run();
    }
}
