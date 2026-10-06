package org.ejavdge.domain.report;

import org.ejavdge.domain.Verdict;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A conditional text that provides feedback based on the outcome of a
 * {@link Verdict}.
 * <p>
 * This class implements {@link Text} and holds two alternative messages: one
 * for a successful verdict and one for a failed verdict. When the content is
 * requested, it inspects the associated {@link Verdict} and returns the
 * corresponding message:
 * <ul>
 *   <li>if the verdict is successful ({@link Verdict#ok()} returns
 *       {@code true}), the success message is returned;</li>
 *   <li>otherwise, the failure message is returned.</li>
 * </ul>
 * This is useful for presenting user-facing feedback that depends on the result
 * of a submission or check.
 */
public final class Feedback implements Text {

    /**
     * The text to display when the verdict is successful.
     */
    private final Text onSuccess;

    /**
     * The text to display when the verdict is not successful.
     */
    private final Text onFail;

    /**
     * The verdict whose outcome determines which message is shown.
     */
    private final Verdict verdict;

    /**
     * Creates a new feedback text with the given success and failure messages,
     * conditioned on the provided verdict.
     *
     * @param s the text to display if the verdict is successful
     * @param f the text to display if the verdict is not successful
     * @param v the verdict whose outcome selects the appropriate message
     */
    public Feedback(final Text s, final Text f, final Verdict v) {
        this.onSuccess = s;
        this.onFail = f;
        this.verdict = v;
    }

    /**
     * Returns the feedback content based on the verdict's outcome.
     * <p>
     * If the verdict is successful, the success message is returned; otherwise,
     * the failure message is returned.
     *
     * @return the selected feedback message as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        if (this.verdict.ok()) {
            return this.onSuccess.content();
        } else {
            return this.onFail.content();
        }
    }
}
