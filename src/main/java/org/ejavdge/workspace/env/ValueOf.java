package org.ejavdge.workspace.env;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class ValueOf implements Text {
    private final EnvVariable src;

    public ValueOf(final EnvVariable v) {
        this.src = v;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.src.value();
    }
}
