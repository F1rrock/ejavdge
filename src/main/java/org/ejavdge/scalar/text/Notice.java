package org.ejavdge.scalar.text;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Text} that performs a side effect before returning its content.
 * <p>
 * This class wraps an {@link Effect} and a {@link Text}. When {@link #content()}
 * is called, it first performs the effect via {@link Effect#perform()}, and then
 * returns the content of the underlying text. This makes it possible to combine
 * an action (such as submitting a solution or sending a request) with a message
 * that describes or confirms the outcome of that action.
 * <p>
 * The effect is performed exactly once per call to {@link #content()}. If the
 * effect throws an {@link InvariantViolation}, the exception propagates and the
 * underlying text is not materialized. This ensures that the message is only
 * shown when the action has completed successfully.
 * <p>
 * This is useful in application entry points where the user should be notified
 * about the result of an operation, for example: perform the submission, then
 * display "Sent!" or "Report is available!".
 */
public final class Notice implements Text {

    /**
     * The effect to perform before returning the text content.
     */
    private final Effect src;

    /**
     * The underlying text whose content is returned after the effect is
     * performed.
     */
    private final Text origin;

    /**
     * Creates a notice that performs the given effect and then returns the given
     * text.
     *
     * @param e the effect to perform when the content is requested
     * @param t the text to return after the effect has been performed
     */
    public Notice(final Effect e, final Text t) {
        this.src = e;
        this.origin = t;
    }

    /**
     * Performs the wrapped effect and then returns the content of the underlying
     * text.
     * <p>
     * The effect is performed first via {@link Effect#perform()}. If it
     * completes successfully, the content of the underlying text is obtained
     * and returned. If the effect throws an {@link InvariantViolation}, the
     * exception is propagated and the text content is not evaluated.
     *
     * @return the content of the underlying text, after the effect has been
     *         performed
     * @throws InvariantViolation if the effect fails or if an invariant is
     *         violated while retrieving the text content
     */
    @Override
    public String content() throws InvariantViolation {
        this.src.perform();
        return this.origin.content();
    }
}
