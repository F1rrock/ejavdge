package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.workspace.out.Console;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WithDetailedReport;
import org.ejavdge.workspace.out.WithStackLog;
import org.slf4j.LoggerFactory;

/**
 * The default {@link Out} used by apps that do not redirect output
 * elsewhere.
 *
 * <p>Writes to {@link Console} — standard output — wrapped in two
 * decorators: {@link WithStackLog} sends a copy of every message to
 * SLF4J for diagnostics, and {@link WithDetailedReport} expands
 * {@code InvariantViolation} messages into a readable report before
 * writing them. This is the {@code Out} wired into apps when the
 * caller does not pass one explicitly.
 *
 * <p>Apps running inside an IDE, or tests that capture output, bypass
 * this class and pass their own {@code Out} to the app constructors.
 */
public final class PresetOut implements Out {
    private final Out origin;

    /**
     * Uses the logger named after this class for stack-trace entries.
     */
    public PresetOut() {
        this.origin = new WithDetailedReport(
            new WithStackLog(
                new Console(),
                LoggerFactory.getLogger(PresetOut.class)
            )
        );
    }

    @Override
    public void write(final Text t) throws InvariantViolation {
        this.origin.write(t);
    }
}
