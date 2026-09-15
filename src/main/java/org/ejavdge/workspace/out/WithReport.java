package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Trimmed;
import org.ejavdge.scalar.text.palette.Red;

public final class WithReport implements Out {
    private final Out origin;
    private final Out err;

    public WithReport(final Out o) {
        this(o, o);
    }

    public WithReport(final Out o, final Out e) {
        this.origin = o;
        this.err = e;
    }

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

    private static final class ReportOfError implements Text {
        private final Throwable error;

        public ReportOfError(final Throwable e) {
            this.error = e;
        }

        @Override
        public String content() throws InvariantViolation {
            final var cause = this.error.getCause();
            if (cause == null) {
                return new Trimmed(
                    new Text.Of(this.error.getMessage())
                ).content();
            }
            return new ReportOfError(cause).content();
        }
    }
}
