package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class WithTimeout implements Bytes {
    private final Bytes origin;
    private final Duration duration;

    public WithTimeout(final Bytes bytes, final Duration timeout) {
        this.origin = bytes;
        this.duration = timeout;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.invokeAny(
                Collections.singleton(this.origin::content),
                this.duration.toNanos(),
                TimeUnit.NANOSECONDS
            );
        } catch (final TimeoutException err) {
            throw new InvariantViolation(
                "Bytes must be obtained within "
                    + this.duration,
                err
            );
        } catch (final InterruptedException err) {
            Thread.currentThread().interrupt();
            throw new InvariantViolation(
                "Bytes could not be obtained within "
                    + this.duration
                    + " because operation interrupted.",
                err
            );
        } catch (final ExecutionException err) {
            throw new InvariantViolation(
                "Bytes content evaluation failed.",
                err
            );
        } finally {
            executor.shutdownNow();
        }
    }
}