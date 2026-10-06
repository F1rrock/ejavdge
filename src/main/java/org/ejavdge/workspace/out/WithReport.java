package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Trimmed;
import org.ejavdge.scalar.text.palette.Red;

/**
 * An {@link Out} decorator that catches {@link InvariantViolation} exceptions
 * thrown during writing and emits a concise, colorized error report to a
 * separate error output instead.
 * <p>
 * This class is similar to {@link WithDetailedReport}, but produces a more
 * compact error message. When the decorated output's {@link Out#write(Text)}
 * method throws an {@link InvariantViolation}, the exception is intercepted and
 * a report is written to the error output. The report is rendered in red (via
 * {@link Red}) and is prefixed with {@code "Error: "}.
 * <p>
 * Instead of rendering the full cause chain, this class follows the chain of
 * {@link InvariantViolation} causes to the deepest one and reports only that
 * deepest exception's message, trimmed of surrounding whitespace. If the
 * exception has no {@link InvariantViolation} cause, its own message is used.
 * This makes the error output easier to read when only the root cause matters.
 * <p>
 * By default, both the normal output and the error output point to the same
 * destination, so errors appear alongside regular output. A separate error
 * output can be supplied to direct errors elsewhere — for example, to
 * {@link System#err} while regular output goes to {@link System#out}.
 */
public final class WithReport implements Out {

    /**
     * The underlying output used for normal, successful writes.
     */
    private final Out origin;

    /**
     * The output used for error reports when writing fails.
     */
    private final Out err;

    /**
     * Creates a decorator that writes errors to the same output as normal text.
     *
     * @param o the output to decorate, used for both normal and error output
     */
    public WithReport(final Out o) {
        this(o, o);
    }

    /**
     * Creates a decorator with separate outputs for normal text and errors.
     *
     * @param o the output used for normal, successful writes
     * @param e the output used for error reports when writing fails
     */
    public WithReport(final Out o, final Out e) {
        this.origin = o;
        this.err = e;
    }

    /**
     * Writes the given text to the decorated output, reporting any failure as a
     * concise error message on the error output.
     * <p>
     * The write is first attempted on the normal output. If it succeeds, the
     * method returns normally. If it throws an {@link InvariantViolation}, the
     * exception is caught and a red, prefixed report is written to the error
     * output instead. The report contains only the message of the deepest
     * {@link InvariantViolation} in the cause chain, trimmed of surrounding
     * whitespace.
     *
     * @param t the text to write
     * @throws InvariantViolation if writing the error report itself fails
     */
    @Override
    public void write(final Text t) throws InvariantViolation {
        try {
            this.origin.write(t);
        } catch (final InvariantViolation e) {
            this.err.write(
                new Red(
                    new Stencil(
                        new Text.Of("Error: %s"),
                        new ReportOfError(e)
                    )
                )
            );
        }
    }

    /**
     * A {@link Text} that renders the deepest {@link InvariantViolation} in a
     * throwable's cause chain as a single, trimmed message.
     * <p>
     * If the throwable's cause is an {@link InvariantViolation}, the rendering
     * recurses into that cause. Otherwise, the throwable's own message is
     * returned, trimmed of leading and trailing whitespace.
     */
    private static final class ReportOfError implements Text {

        /**
         * The throwable whose deepest {@link InvariantViolation} message is
         * rendered.
         */
        private final Throwable error;

        /**
         * Creates a report for the given throwable.
         *
         * @param e the throwable to render
         */
        public ReportOfError(final Throwable e) {
            this.error = e;
        }

        /**
         * Returns the rendered error message, following the cause chain to the
         * deepest {@link InvariantViolation}.
         * <p>
         * If the throwable has an {@link InvariantViolation} cause, the
         * rendering recurses into that cause. Otherwise, the throwable's own
         * message is returned, trimmed of surrounding whitespace.
         *
         * @return the deepest error message, trimmed
         * @throws InvariantViolation if an invariant is violated while
         *         materializing the message or its cause
         */
        @Override
        public String content() throws InvariantViolation {
            final var cause = this.error.getCause();
            if (cause instanceof InvariantViolation) {
                return new ReportOfError(cause).content();
            }
            return new Trimmed(
                new Text.Of(this.error.getMessage())
            ).content();
        }
    }
}
