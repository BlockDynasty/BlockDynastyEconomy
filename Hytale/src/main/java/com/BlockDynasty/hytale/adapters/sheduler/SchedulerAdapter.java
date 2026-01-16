package com.BlockDynasty.hytale.adapters.sheduler;

import abstractions.platform.scheduler.ContextualTask;
import abstractions.platform.scheduler.IScheduler;

import java.util.concurrent.CompletableFuture;

public class SchedulerAdapter implements IScheduler {
    @Override
    public void runLater(long l, ContextualTask contextualTask) {
        contextualTask.getRunnable().run();
        CompletableFuture.delayedExecutor(l, java.util.concurrent.TimeUnit.MILLISECONDS)
                .execute(contextualTask.getRunnable());
    }

    @Override
    public void runAsync(ContextualTask contextualTask) {
        contextualTask.getRunnable().run();

    }

    @Override
    public void run(ContextualTask contextualTask) {
        contextualTask.getRunnable().run();
        //CompletableFuture.runAsync(contextualTask.getRunnable());
    }
}
