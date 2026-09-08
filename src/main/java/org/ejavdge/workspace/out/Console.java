package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.PrintStream;

public final class Console implements Out {
    private final PrintStream src;

    public Console() {
        this(System.out);
    }

    public Console(final PrintStream s) {
        this.src = s;
    }

    @Override
    public void write(final Text t) throws InvariantViolation {
        this.src.print(t.content());
    }
}
