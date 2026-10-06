package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * An effect that delays the execution of another effect.
 * <p>
 * This class wraps an {@link Effect} and a {@link Duration}, and performs the
 * wrapped effect only after the specified delay has elapsed. The delay is
 * implemented using a {@link ScheduledExecutorService}: a {@link CountDownLatch}
 * is scheduled to be released after the given duration, and the current thread
 * waits on it before invoking the underlying effect.
 * <p>
 * The scheduler is provided by a {@link Supplier}, allowing callers to inject
 * a custom executor service if needed. By default, a new single-threaded
 * scheduled executor is created for each invocation. Regardless of the outcome,
 * the scheduler down once the effect has been performed, so no leaked threads.
 * <p>
 * If the waiting thread is interrupted, the interrupt flag is restored on the
 * current thread and an {@link InvariantViolation} is thrown.
 */
public final class WithDelay implements Effect {

    /**
     * The effect to be performed after the delay.
     */
    private final Effect origin;

    /**
     * The duration of the delay.
     */
    private final Duration delay;

    /**
     * The supplier of the scheduler used to implement the delay.
     */
    private final Supplier<ScheduledExecutorService> scheduler;

    /**
     * Creates a delayed effect using a default single-threaded scheduler.
     *
     * @param e the effect to perform after the delay
     * @param d the duration of the delay
     */
    public WithDelay(final Effect e, final Duration d) {
        this(e, d, Executors::newSingleThreadScheduledExecutor);
    }

    /**
     * Creates a delayed effect using the given scheduler supplier.
     *
     * @param e the effect to perform after the delay
     * @param d the duration of the delay
     * @param s the supplier of the scheduler used to implement the delay
     */
    public WithDelay(final Effect e, final Duration d, final Supplier<ScheduledExecutorService> s) {
        this.origin = e;
        this.delay = d;
        this.scheduler = s;
    }

    /**
     * Performs this effect after waiting for the configured delay.
     * <p>
     * A scheduler is obtained from the supplier, and a {@link CountDownLatch} is
     * scheduled to be released after the delay has elapsed. The current thread
     * waits on the latch, and once released, the underlying effect is performed.
     * The scheduler is always shut down after the operation, whether it
     * succeeds or fails.
     * <p>
     * If there is interrupted thread while waiting, the interrupt flag is set and
     * an {@link InvariantViolation} is thrown.
     *
     * @throws InvariantViolation if the wait is interrupted or the underlying
     *         effect violates an invariant during execution
     */
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
