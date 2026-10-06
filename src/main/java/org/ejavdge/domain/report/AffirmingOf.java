package org.ejavdge.domain.report;

import org.ejavdge.domain.Verdict;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * An effect that affirms a verdict by checking its success status.
 * <p>
 * This effect wraps a {@link Verdict} and a message. When performed, it
 * verifies that the verdict is successful (i.e., {@link Verdict#ok()} returns
 * {@code true}). If the verdict is not successful, it throws an
 * {@link InvariantViolation} with the provided message as its content.
 * <p>
 * This is useful as a guard or post-condition check: after an operation that
 * produces a verdict, this effect can be used to fail fast if the operation did
 * not succeed. If the verdict is successful, the effect does nothing.
 */
public final class AffirmingOf implements Effect {

    /**
     * The verdict to be affirmed.
     */
    private final Verdict src;

    /**
     * The message to include in the exception if the verdict is not successful.
     */
    private final Text message;

    /**
     * Creates a new affirming effect for the given verdict and failure message.
     *
     * @param v the verdict to affirm
     * @param m the message to use if the verdict is not successful
     */
    public AffirmingOf(final Verdict v, final Text m) {
        this.src = v;
        this.message = m;
    }

    /**
     * Performs this effect by checking the verdict's success status.
     * <p>
     * If the verdict is not successful, an {@link InvariantViolation} is thrown
     * with the content of the provided message.
     *
     * @throws InvariantViolation if the verdict is not successful
     */
    @Override
    public void perform() throws InvariantViolation {
        if (!this.src.ok()) {
            throw new InvariantViolation(this.message.content());
        }
    }
}
