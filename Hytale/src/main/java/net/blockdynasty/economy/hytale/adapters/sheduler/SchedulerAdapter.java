package net.blockdynasty.economy.hytale.adapters.sheduler;

import com.hypixel.hytale.server.core.HytaleServer;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ContextualTask;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.IScheduler;

import java.util.concurrent.CompletableFuture;

public class SchedulerAdapter implements IScheduler {
    @Override
    public void runLater(long l, ContextualTask contextualTask) {
        //contextualTask.getRunnable().run();
        CompletableFuture.delayedExecutor(l, java.util.concurrent.TimeUnit.MILLISECONDS)
                .execute(contextualTask.getRunnable());
    }

    @Override
    public void runAsync(ContextualTask contextualTask) {

        contextualTask.getRunnable().run();
        //CompletableFuture.runAsync(contextualTask.getRunnable());
        //HytaleServer.SCHEDULED_EXECUTOR.submit(contextualTask.getRunnable());

    }

    @Override
    public void run(ContextualTask contextualTask) {
        contextualTask.getRunnable().run();
        //CompletableFuture.runAsync(contextualTask.getRunnable());
        //HytaleServer.SCHEDULED_EXECUTOR.submit(contextualTask.getRunnable());
    }
}
