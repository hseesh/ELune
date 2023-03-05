package com.yatoufang.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hse
 * @since 2023/3/4
 */
public class ELuneScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    private static final ELuneScheduledThreadPoolExecutor instance = new ELuneScheduledThreadPoolExecutor();

    public static ELuneScheduledThreadPoolExecutor getInstance() {
        return instance;
    }

    private ELuneScheduledThreadPoolExecutor() {
        super(1);
    }

    @Override
    public ScheduledFuture<?> schedule(@NotNull Runnable runnable, long delay, @NotNull TimeUnit unit) {
        return super.schedule(() -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw throwable;
            }
        }, delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(@NotNull Callable<V> callable, long delay, @NotNull TimeUnit unit) {
        return super.schedule(() -> {
            try {
                return callable.call();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw throwable;
            }
        }, delay, unit);
    }
}