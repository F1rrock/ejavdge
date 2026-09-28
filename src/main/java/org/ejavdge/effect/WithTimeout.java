package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

public final class WithTimeout implements Effect {
    private final Effect origin;
    private final Duration timeout;
    private final Supplier<ExecutorService> executor;

    public WithTimeout(final Effect e, final Duration t) {
        this(e, t, Executors::newSingleThreadExecutor);
    }

    public WithTimeout(final Effect e, final Duration t, Supplier<ExecutorService> s) {
        this.origin = e;
        this.timeout = t;
        this.executor = s;
    }

    @Override
    @SuppressWarnings("PMD.PreserveStackTrace")
    public void perform() throws InvariantViolation {
        final var pool = this.executor.get();
        try {
            try {
                pool
                    .submit(this.origin::perform)
                    .get(this.timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InvariantViolation(
                    "Effect could not be performed " +
                        "because the operation was interrupted.",
                    e
                );
            } catch (final ExecutionException e) {
                throw new InvariantViolation(
                    "Effect could not be performed " +
                        "because the origin failed.",
                    e.getCause()
                );
            } catch (final TimeoutException e) {
                throw new InvariantViolation(
                    "Effect not performed within the allowed time.",
                    e
                );
            }
        } finally {
            pool.shutdownNow();
        }
    }
}
