package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Trimmed;
import org.ejavdge.scalar.text.palette.Red;

public final class WithReport implements Out {
    private final Out origin;

    public WithReport(final Out o) {
        this.origin = o;
    }

    @Override
    public void write(final Text t) throws InvariantViolation {
        try {
            this.origin.write(t);
        } catch (final InvariantViolation e) {
            this.origin.write(
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
            final var message = this.error.getMessage();
            if (cause == null) {
                return message;
            }
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
    }
}
