package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * A {@link Bytes} decorator that materializes the underlying byte content with
 * a timeout.
 * <p>
 * This class wraps another {@link Bytes} instance and a timeout
 * {@link Duration}. When {@link #content()} is called, it submits the request
 * for the underlying content to an {@link ExecutorService} and waits for its
 * completion up to the specified timeout. If the content is obtained in time,
 * it is returned. Otherwise, an {@link InvariantViolation} is thrown:
 * <ul>
 *   <li>if the waiting thread is interrupted, an {@code InvariantViolation} is
 *       thrown and the interrupt flag is restored;</li>
 *   <li>if the underlying sequence throws an exception during evaluation, the
 *       cause is wrapped in an {@code InvariantViolation};</li>
 *   <li>if the timeout expires before the content is obtained, an
 *       {@code InvariantViolation} is thrown.</li>
 * </ul>
 * <p>
 * The executor is provided by a {@link Supplier}, allowing callers to inject a
 * custom executor service if needed. By default, a new single-threaded executor
 * is created for each invocation. Regardless of the outcome, the executor is
 * shut down once the operation has finished, so no threads are leaked.
 * <p>
 * This is useful for bounding the time spent on potentially blocking
 * operations, such as network requests to a remote contest system, so that the
 * application does not hang indefinitely if the remote endpoint becomes
 * unresponsive.
 */
public final class WithTimeout implements Bytes {

    /**
     * The underlying byte sequence whose content is obtained with a timeout.
     */
    private final Bytes origin;

    /**
     * The maximum duration allowed for obtaining the content.
     */
    private final Duration timeout;

    /**
     * The supplier of the executor used to evaluate the content asynchronously.
     */
    private final Supplier<ExecutorService> executor;

    /**
     * Creates a timeout wrapper using a default single-threaded executor.
     *
     * @param bs the underlying byte sequence
     * @param t  the maximum duration allowed for obtaining the content
     */
    public WithTimeout(final Bytes bs, final Duration t) {
        this(bs, t, Executors::newSingleThreadExecutor);
    }

    /**
     * Creates a timeout wrapper using the given executor supplier.
     *
     * @param bs the underlying byte sequence
     * @param t  the maximum duration allowed for obtaining the content
     * @param e  the supplier of the executor used to evaluate the content
     *           asynchronously
     */
    public WithTimeout(final Bytes bs, final Duration t, Supplier<ExecutorService> e) {
        this.origin = bs;
        this.timeout = t;
        this.executor = e;
    }

    /**
     * Returns the content of the underlying byte sequence, bounded by the
     * configured timeout.
     * <p>
     * The request for the underlying content is submitted to an executor and
     * awaited up to the specified timeout. If the content is obtained in time,
     * it is returned normally. If the waiting thread is interrupted, the
     * interrupt flag is set and an {@link InvariantViolation} is thrown. If the
     * underlying evaluation throws an exception, that exception wrapped in
     * an {@code InvariantViolation}. If the timeout expires before the content
     * obtained, an {@code InvariantViolation} is thrown as well. The
     * executor always shut down after the operation.
     *
     * @return the byte content of the underlying sequence
     * @throws InvariantViolation if the content is not obtained within the
     *         allowed time, the operation is interrupted, or the underlying
     *         evaluation fails
     */
    @Override
    @SuppressWarnings("PMD.PreserveStackTrace")
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
                    e.getCause()
                );
            } catch (final TimeoutException e) {
                throw new InvariantViolation(
                    "Bytes not obtained within the allowed time.",
                    e
                );
            }
        } finally {
            pool.shutdownNow();
        }
    }
}
