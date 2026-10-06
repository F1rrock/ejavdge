package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.slf4j.Logger;

/**
 * An {@link Out} decorator that logs the stack trace of any
 * {@link InvariantViolation} thrown during writing, and then rethrows it.
 * <p>
 * This decorator is intended for debugging purposes. It wraps an output and a
 * {@link Logger}, and when the decorated output's {@link Out#write(Text)}
 * method throws an {@link InvariantViolation}, it logs the full stack trace at
 * the {@code TRACE} level (using the message {@code "Stack trace:"}) before
 * rethrowing the same exception.
 * <p>
 * Because the exception is rethrown unchanged, the behavior of the application
 * is not altered: higher-level handlers (such as {@link WithReport} or
 * {@link WithDetailedReport}) can still catch the exception and present a
 * user-friendly error message. At the same time, the stack trace is recorded
 * for later inspection, which is valuable when diagnosing failures that might
 * otherwise be reported only as a short message.
 * <p>
 * Logging only occurs if the logger's {@code TRACE} level is enabled, so in
 * production configurations where trace logging is disabled, this decorator
 * adds negligible overhead.
 */
public final class WithStackLog implements Out {

    /**
     * The underlying output to which text is written.
     */
    private final Out origin;

    /**
     * The logger used to record stack traces of any thrown
     * {@link InvariantViolation}.
     */
    private final Logger log;

    /**
     * Creates a stack-logging decorator around the given output using the
     * specified logger.
     *
     * @param o the underlying output to decorate
     * @param l the logger used to log stack traces at trace level
     */
    public WithStackLog(final Out o, final Logger l) {
        this.origin = o;
        this.log = l;
    }

    /**
     * Writes the given text to the decorated output, logging the stack trace of
     * any {@link InvariantViolation} that occurs and then rethrowing it.
     * <p>
     * The writing is attempted on the underlying output. If it succeeds, the
     * method returns normally. If it throws an {@link InvariantViolation}, the
     * exception is logged at the {@code TRACE} level with the message
     * {@code "Stack trace:"}, and then rethrown so that higher-level handlers
     * can process it as usual.
     *
     * @param t the text to write
     * @throws InvariantViolation if the underlying write fails; the exception is
     *         logged and then rethrown unchanged
     */
    @Override
    public void write(Text t) throws InvariantViolation {
        try {
            this.origin.write(t);
        } catch (final InvariantViolation e) {
            log.trace("Stack trace:", e);
            throw e;
        }
    }
}
