package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.slf4j.Logger;

public final class WithStackLog implements Out {
    private final Out origin;
    private final Logger log;

    public WithStackLog(final Out o, final Logger l) {
        this.origin = o;
        this.log = l;
    }

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
