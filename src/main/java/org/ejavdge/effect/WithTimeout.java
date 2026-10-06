package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * An effect that executes another effect with a timeout.
 * <p>
 * This class wraps an {@link Effect} and a timeout {@link Duration}. When
 * performed, it submits the wrapped effect to an {@link ExecutorService} and
 * waits for its completion up to the specified timeout. If the effect completes
 * successfully within the allowed time, the method returns normally. Otherwise,
 * an {@link InvariantViolation} is thrown:
 * <ul>
 *   <li>if the waiting thread is interrupted, an {@code InvariantViolation} is
 *       thrown and the interrupt flag is restored;</li>
 *   <li>if the effect throws an exception during execution, the cause is
 *       wrapped in an {@code InvariantViolation};</li>
 *   <li>if the timeout expires before the effect completes, an
 *       {@code InvariantViolation} is thrown.</li>
 * </ul>
 * <p>
 * The executor is provided by a {@link Supplier}, allowing callers to inject a
 * custom executor service if needed. By default, a new single-threaded executor
 * is created for each invocation. Regardless of the outcome, the executor is
 * shut down once the operation has finished, so no threads are leaked.
 */
public final class WithTimeout implements Effect {

    /**
     * The effect to be performed with a timeout.
     */
    private final Effect origin;

    /**
     * The maximum duration allowed for the effect to complete.
     */
    private final Duration timeout;

    /**
     * The supplier of the executor used to run the effect asynchronously.
     */
    private final Supplier<ExecutorService> executor;

    /**
     * Creates a timeout effect using a default single-threaded executor.
     *
     * @param e the effect to perform with a timeout
     * @param t the maximum duration allowed for the effect to complete
     */
    public WithTimeout(final Effect e, final Duration t) {
        this(e, t, Executors::newSingleThreadExecutor);
    }

    /**
     * Creates a timeout effect using the given executor supplier.
     *
     * @param e the effect to perform with a timeout
     * @param t the maximum duration allowed for the effect to complete
     * @param s the supplier of the executor used to run the effect
     *          asynchronously
     */
    public WithTimeout(final Effect e, final Duration t, Supplier<ExecutorService> s) {
        this.origin = e;
        this.timeout = t;
        this.executor = s;
    }

    /**
     * Performs this effect with the configured timeout.
     * <p>
     * The wrapped effect is submitted to an executor and awaited up to the
     * specified timeout. If the effect completes successfully, this method
     * returns normally. If the waiting thread is interrupted, the interrupt
     * flag is set and an {@link InvariantViolation} is thrown. If the effect
     * throws an exception, that exception is wrapped in an
     * {@code InvariantViolation}. If the timeout expires before the effect
     * completes, an {@code InvariantViolation} is thrown as well. The executor
     * always shut down after the operation.
     *
     * @throws InvariantViolation if the effect does not complete within the
     *         allowed time, is interrupted, or fails during execution
     */
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
