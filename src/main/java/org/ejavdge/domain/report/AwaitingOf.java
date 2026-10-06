package org.ejavdge.domain.report;

import org.ejavdge.domain.Verdict;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.effect.WithDelay;

import java.time.Duration;

/**
 * An effect that repeatedly checks a verdict until it becomes successful,
 * pausing between attempts.
 * <p>
 * This effect wraps a {@link Verdict} and a delay {@link Duration}. When
 * performed, it checks whether the verdict is successful (i.e.,
 * {@link Verdict#ok()} returns {@code true}):
 * <ul>
 *   <li>if the verdict is already successful, the effect does nothing and
 *       returns immediately;</li>
 *   <li>otherwise, it schedules itself to be performed again after the given
 *       delay via {@link WithDelay}, effectively polling the verdict until it
 *       becomes successful.</li>
 * </ul>
 * <p>
 * This is useful for waiting on the result of an asynchronous operation whose
 * status is exposed through a {@link Verdict}, such as a submitted solution
 * that is being checked by the contest system.
 */
public final class AwaitingOf implements Effect {

    /**
     * The verdict being awaited.
     */
    private final Verdict verdict;

    /**
     * The delay between successive checks of the verdict.
     */
    private final Duration duration;

    /**
     * Creates a new awaiting effect for the given verdict and delay.
     *
     * @param v the verdict to wait for
     * @param d the delay between successive checks
     */
    public AwaitingOf(final Verdict v, final Duration d) {
        this.verdict = v;
        this.duration = d;
    }

    /**
     * Performs this effect by waiting until the verdict becomes successful.
     * <p>
     * If the verdict is already successful, this method returns immediately.
     * Otherwise, it reschedules itself to run again after the configured delay,
     * thereby polling the verdict at regular intervals until it succeeds.
     *
     * @throws InvariantViolation if an invariant is violated while checking the
     *         verdict or scheduling the next attempt
     */
    @Override
    public void perform() throws InvariantViolation {
        if (!this.verdict.ok()) {
            new WithDelay(this, this.duration).perform();
        }
    }
}
