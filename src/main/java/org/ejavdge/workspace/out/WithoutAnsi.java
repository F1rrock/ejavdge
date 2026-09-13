package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonAnsiText;
import org.ejavdge.scalar.text.Text;

public final class WithoutAnsi implements Out {
    private final Out origin;

    public WithoutAnsi(final Out o) {
        this.origin = o;
    }

    @Override
    public void write(final Text t) throws InvariantViolation {
        this.origin.write(new NonAnsiText(t));
    }
}
