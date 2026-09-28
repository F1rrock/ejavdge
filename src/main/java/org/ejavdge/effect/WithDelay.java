package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

public final class WithDelay implements Effect {
    private final Effect origin;
    private final Duration delay;
    private final Supplier<ScheduledExecutorService> scheduler;

    public WithDelay(final Effect e, final Duration d) {
        this(e, d, Executors::newSingleThreadScheduledExecutor);
    }

    public WithDelay(final Effect e, final Duration d, final Supplier<ScheduledExecutorService> s) {
        this.origin = e;
        this.delay = d;
        this.scheduler = s;
    }

    @Override
    public void perform() throws InvariantViolation {
        final var pool = this.scheduler.get();
        try {
            final var latch = new CountDownLatch(1);
            pool.schedule(latch::countDown, this.delay.toMillis(), TimeUnit.MILLISECONDS);
            try {
                latch.await();
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InvariantViolation(
                    "There is no content because the delay was interrupted",
                    e
                );
            }
            this.origin.perform();
        } finally {
            pool.shutdownNow();
        }
    }
}
