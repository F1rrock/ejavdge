package org.ejavdge.workspace.out;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class WritingOf implements Effect {
    private final Text message;
    private final Out out;

    public WritingOf(final Text t, final Out o) {
        this.message = t;
        this.out = o;
    }

    @Override
    public void perform() throws InvariantViolation {
        this.out.write(this.message);
    }
}
