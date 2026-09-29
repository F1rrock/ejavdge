package org.ejavdge.effect;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class WithDelayTest extends TestCase {
    private final Supplier<ScheduledExecutorService> instant = InstantScheduler::new;

    public void testOriginContent() {
        final var calls = new AtomicInteger(0);
        new WithDelay(
            calls::incrementAndGet,
            Duration.ofMillis(1),
            this.instant
        ).perform();
        assertEquals(1, calls.get());
    }

    public void testDelayBeforeOrigin() {
        final var order = new StringBuilder();
        new WithDelay(
            () -> order.append("text"),
            Duration.ofMillis(1),
            () -> new RecordingScheduler(order)
        ).perform();
        assertEquals("delay,text", order.toString());
    }

    public void testZeroDelay() {
        final var calls = new AtomicInteger(0);
        new WithDelay(
            calls::incrementAndGet,
            Duration.ZERO,
            this.instant
        ).perform();
        assertEquals(1, calls.get());
    }

    public void testBrokenOrigin() {
        try {
            new WithDelay(
                () -> {
                    throw new InvariantViolation("There is no effect.");
                },
                Duration.ofMillis(1),
                this.instant
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testSchedulerShutdown() throws InterruptedException {
        try (final var pool = new InstantScheduler()) {
            new WithDelay(
                () -> {},
                Duration.ofMillis(1),
                () -> pool
            ).perform();
            assertTrue(
                pool.awaitTermination(100, TimeUnit.MILLISECONDS)
                    && pool.isShutdown()
            );
        }
    }

    public void testSchedulerShutdownWithBrokenOrigin()
        throws InterruptedException {
        try (final var pool = new InstantScheduler()) {
            try {
                new WithDelay(
                    () -> {
                        throw new InvariantViolation("There is no effect.");
                    },
                    Duration.ofMillis(1),
                    () -> pool
                ).perform();
            } catch (final InvariantViolation ignored) {
                assertTrue(pool.awaitTermination(100, TimeUnit.MILLISECONDS));
                assertTrue(pool.isShutdown());
                return;
            }
            fail("InvariantViolation");
        }
    }

    public void testSchedulerObtainedEachCall() {
        final var calls = new AtomicInteger(0);
        final var notice = new WithDelay(
            () -> {},
            Duration.ofMillis(1),
            () -> {
                calls.incrementAndGet();
                return new InstantScheduler();
            }
        );
        notice.perform();
        notice.perform();
        assertEquals(2, calls.get());
    }

    private static final class InstantScheduler
            extends ScheduledThreadPoolExecutor
            implements AutoCloseable {
        InstantScheduler() {
            super(1);
        }

        @Override
        public ScheduledFuture<?> schedule(
            final Runnable command,
            final long delay,
            final TimeUnit unit
        ) {
            command.run();
            return super.schedule(
                () -> {},
                0,
                TimeUnit.MILLISECONDS
            );
        }

        @Override
        public void close() {
            super.shutdown();
        }
    }

    private static final class RecordingScheduler
            extends ScheduledThreadPoolExecutor {
        private final StringBuilder log;

        RecordingScheduler(final StringBuilder l) {
            super(1);
            this.log = l;
        }

        @Override
        public ScheduledFuture<?> schedule(
            final Runnable command,
            final long delay,
            final TimeUnit unit
        ) {
            this.log.append("delay,");
            command.run();
            return super.schedule(
                () -> {},
                0,
                TimeUnit.MILLISECONDS
            );
        }
    }
}
