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

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import abstractions.platform.scheduler.ContextualTask;
import abstractions.platform.scheduler.IScheduler;
import org.bukkit.Bukkit;

public class SchedulerBukkit implements IScheduler {
    public static BlockDynastyEconomy plugin = BlockDynastyEconomy.getInstance();

    public void runLater(long delay, ContextualTask contextualTask) {
        Bukkit.getScheduler().runTaskLater(plugin, contextualTask.getRunnable(), delay);
    }

    public void runAsync(ContextualTask contextualTask) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, contextualTask.getRunnable());
    }

    public void run(ContextualTask contextualTask) {
        Bukkit.getScheduler().runTask(plugin, contextualTask.getRunnable());
    }

    public static SchedulerBukkit init() {
        return new SchedulerBukkit();
    }

}



/*
    public static void runLaterAsync(long delay, Runnable runnable)
    {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    public static void runAtInterval(long interval, Runnable... tasks)
    {
        runAtInterval(0L, interval, tasks);
    }

    public static void runAtInterval(long delay, long interval, Runnable... tasks)
    {
        new BukkitRunnable()
        {
            private int index;

            @Override
            public void run()
            {
                if (this.index >= tasks.length)
                {
                    this.cancel();
                    return;
                }

                tasks[index].run();
                index++;
            }
        }.runTaskTimer(plugin, delay, interval);
    }

    public static void repeat(int repetitions, long interval, Runnable task, Runnable onComplete)
    {
        new BukkitRunnable()
        {
            private int index;

            @Override
            public void run()
            {
                index++;
                if (this.index >= repetitions)
                {
                    this.cancel();
                    if (onComplete == null)
                    {
                        return;
                    }

                    onComplete.run();
                    return;
                }

                task.run();
            }
        }.runTaskTimer(plugin, 0L, interval);
    }

    public static void repeatWhile(long interval, Callable<Boolean> predicate, Runnable task, Runnable onComplete)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (!predicate.call())
                    {
                        this.cancel();
                        if (onComplete == null)
                        {
                            return;
                        }

                        onComplete.run();
                        return;
                    }

                    task.run();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(plugin, 0L, interval);
    }

    /*public interface Task
    {
        void start(Runnable onComplete);
    }



    public static class TaskBuilder
    {
        private Queue<Task> taskList;

        public TaskBuilder()
        {
            this.taskList = new LinkedList<>();
        }

        public TaskBuilder append(TaskBuilder builder)
        {
            this.taskList.addAll(builder.taskList);
            return this;
        }

        public TaskBuilder appendDelay(long delay)
        {
            this.taskList.add(onComplete -> SchedulerUtils.runLater(delay, onComplete));
            return this;
        }

        public TaskBuilder appendTask(Runnable task)
        {
            this.taskList.add(onComplete ->
            {
                task.run();
                onComplete.run();
            });

            return this;
        }

        public TaskBuilder appendTask(Task task)
        {
            this.taskList.add(task);
            return this;
        }

        public TaskBuilder appendDelayedTask(long delay, Runnable task)
        {
            this.taskList.add(onComplete -> SchedulerUtils.runLater(delay, () ->
            {
                task.run();
                onComplete.run();
            }));

            return this;
        }

        public TaskBuilder appendTasks(long delay, long interval, Runnable... tasks)
        {
            this.taskList.add(onComplete ->
            {
                Runnable[] runnables = Arrays.copyOf(tasks, tasks.length + 1);
                runnables[runnables.length - 1] = onComplete;
                SchedulerUtils.runAtInterval(delay, interval, runnables);
            });

            return this;
        }

        public TaskBuilder appendRepeatingTask(int repetitions, long interval, Runnable task)
        {
            this.taskList.add(onComplete -> SchedulerUtils.repeat(repetitions, interval, task, onComplete));
            return this;
        }

        public TaskBuilder appendConditionalRepeatingTask(long interval, Callable<Boolean> predicate, Runnable task)
        {
            this.taskList.add(onComplete -> SchedulerUtils.repeatWhile(interval, predicate, task, onComplete));
            return this;
        }

        public TaskBuilder waitFor(Callable<Boolean> predicate)
        {
            this.taskList.add(onComplete -> new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        if (!predicate.call())
                        {
                            return;
                        }

                        this.cancel();
                        onComplete.run();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L));
            return this;
        }

        public void runTasks()
        {
            this.startNext();
        }

        private void startNext()
        {
            Task task = this.taskList.poll();
            if (task == null)
            {
                return;
            }

            task.start(this::startNext);
        }
    }*/

