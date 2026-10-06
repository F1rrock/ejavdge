package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Trimmed;
import org.ejavdge.scalar.text.palette.Red;

/**
 * An {@link Out} decorator that catches {@link InvariantViolation} exceptions
 * thrown during writing and emits a detailed, colorized error report to a
 * separate error output instead.
 * <p>
 * This class is designed to make failures visible to the user in a friendly and
 * informative way. When the decorated output's {@link Out#write(Text)} method
 * throws an {@link InvariantViolation}, the exception is intercepted and a
 * report is written to the error output. The report is rendered in red (via
 * {@link Red}) and is prefixed with {@code "Error: "}. If the exception has a
 * cause that is itself an {@link InvariantViolation}, the report expanded
 * to include the cause chain, with each nested cause labelled as
 * {@code "Caused by: "}.
 * <p>
 * By default, both the normal output and the error output point to the same
 * destination, so errors appear alongside regular output. A separate error
 * output can be supplied to direct errors elsewhere — for example, to
 * {@link System#err} while regular output goes to {@link System#out}.
 */
public final class WithDetailedReport implements Out {

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
    public WithDetailedReport(final Out o) {
        this(o, o);
    }

    /**
     * Creates a decorator with separate outputs for normal text and errors.
     *
     * @param o the output used for normal, successful writes
     * @param e the output used for error reports when writing fails
     */
    public WithDetailedReport(final Out o, final Out e) {
        this.origin = o;
        this.err = e;
    }

    /**
     * Writes the given text to the decorated output, reporting any failure as a
     * detailed error message on the error output.
     * <p>
     * The writing is first attempted on the normal output. If it succeeds, the
     * method returns normally. If it throws an {@link InvariantViolation}, the
     * exception is caught and a red, prefixed report is written to the error
     * output instead. The report includes the exception's message and, if its
     * cause is another {@link InvariantViolation}, a nested {@code "Caused by:"}
     * chain is included as well.
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
     * A {@link Text} that renders a {@link Throwable} as a detailed, multi-line
     * error message, following the cause chain of {@link InvariantViolation}
     * instances.
     * <p>
     * If the throwable's cause is an {@link InvariantViolation}, the rendered
     * text includes the throwable's message followed by a {@code "Caused by: "}
     * line that recursively renders the cause. If the cause is not an
     * {@link InvariantViolation} (or is {@code null}), only the throwable's own
     * message is returned.
     */
    private static final class ReportOfError implements Text {

        /**
         * The throwable whose message is rendered.
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
         * Returns the rendered error report.
         * <p>
         * If the throwable has an {@link InvariantViolation} cause, the report
         * includes the throwable's message followed by a newline and a
         * {@code "Caused by: "} line rendering that cause. Otherwise, only the
         * throwable's own message is returned.
         *
         * @return the rendered error message
         * @throws InvariantViolation if an invariant is violated while
         *         materializing the message or its cause
         */
        @Override
        public String content() throws InvariantViolation {
            final var cause = this.error.getCause();
            final var message = this.error.getMessage();
            if (cause instanceof InvariantViolation) {
                return new Concat(
                    new Text.Of("\n"),
                    new Items.Of<>(
                        new Trimmed(
                            new Text.Of(message)
                        ),
                        new Stencil(
                            new Text.Of("Caused by: %s"),
                            new ReportOfError(cause)
                        )
                    )
                ).content();
            }
            return message;
        }
    }
}
