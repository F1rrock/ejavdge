package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class Timeout implements Bytes {
    private final Bytes origin;
    private final Duration duration;

    public Timeout(final Bytes bytes, final Duration timeout) {
        this.origin = bytes;
        this.duration = timeout;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            final Future<byte[]> future = executor.submit(this.origin::content);
            try {
                return future.get(
                    this.duration.toNanos(),
                    TimeUnit.NANOSECONDS
                );
            } catch (final TimeoutException err) {
                future.cancel(true);
                throw new InvariantViolation(
                    "Bytes content evaluation timed out after "
                    + this.duration
                );
            } catch (final InterruptedException err) {
                future.cancel(true);
                Thread.currentThread().interrupt();
                throw new InvariantViolation(
                    "Bytes content evaluation was interrupted."
                );
            } catch (final ExecutionException err) {
                final Throwable cause = err.getCause();
                if (cause instanceof InvariantViolation) {
                    throw (InvariantViolation) cause;
                }
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                if (cause instanceof Error) {
                    throw (Error) cause;
                }
                throw new IllegalStateException(cause);
            }
        } finally {
            executor.shutdownNow();
        }
    }
}