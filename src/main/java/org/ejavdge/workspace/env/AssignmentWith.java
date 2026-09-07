package org.ejavdge.workspace.env;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class AssignmentWith implements Effect {
    private final Text value;
    private final EnvVariable variable;

    public AssignmentWith(final Text t, final EnvVariable v) {
        this.value = t;
        this.variable = v;
    }

    @Override
    public void perform() throws InvariantViolation {
        this.variable.assignWith(this.value);
    }
}
