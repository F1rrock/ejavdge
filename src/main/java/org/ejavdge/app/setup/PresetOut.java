package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.workspace.out.Console;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WithReport;
import org.ejavdge.workspace.out.WithStackLog;
import org.slf4j.LoggerFactory;

public final class PresetOut implements Out {
    private final Out origin;

    public PresetOut() {
        this.origin = new WithReport(
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
