package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

public final class WithTimeout implements Bytes {
    private final Bytes origin;
    private final Duration timeout;
    private final Supplier<ExecutorService> executor;

    public WithTimeout(final Bytes bs, final Duration t) {
        this(bs, t, Executors::newSingleThreadExecutor);
    }

    public WithTimeout(final Bytes bs, final Duration t, Supplier<ExecutorService> e) {
        this.origin = bs;
        this.timeout = t;
        this.executor = e;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        final var pool = this.executor.get();
        try {
            try {
                return pool
                    .submit(this.origin::content)
                    .get(this.timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InvariantViolation(
                    "Bytes could not be obtained " +
                        "because the operation was interrupted.",
                    e
                );
            } catch (final ExecutionException e) {
                throw new InvariantViolation(
                    "Bytes could not be obtained " +
                        "because the origin failed.",
                    e
                );
            } catch (final TimeoutException e) {
                throw new InvariantViolation(
                    "Bytes not obtained within the allowed time.",
                    e
                );
            }
        } finally {
            this.executor.get().shutdownNow();
        }
    }
}
