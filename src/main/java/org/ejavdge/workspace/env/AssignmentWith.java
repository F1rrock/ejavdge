package org.ejavdge.workspace.env;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * An effect that assigns a textual value to an environment variable.
 * <p>
 * This class implements {@link Effect} and represents the action of setting an
 * {@link EnvVariable} to a given {@link Text} value. When performed, it
 * delegates to {@link EnvVariable#assignWith(Text)} to update the variable.
 * <p>
 * This is useful for configuring the workspace environment dynamically, for
 * example to set a variable that will be used by subsequent effects or by the
 * contest system when running a solution.
 */
public final class AssignmentWith implements Effect {

    /**
     * The value to assign to the environment variable.
     */
    private final Text value;

    /**
     * The environment variable to be assigned.
     */
    private final EnvVariable variable;

    /**
     * Creates an assignment effect that will set the given environment variable
     * to the specified value.
     *
     * @param t the value to assign
     * @param v the environment variable to assign to
     */
    public AssignmentWith(final Text t, final EnvVariable v) {
        this.value = t;
        this.variable = v;
    }

    /**
     * Performs this effect by assigning the value to the environment variable.
     * <p>
     * The assignment is delegated to
     * {@link EnvVariable#assignWith(Text)}, which updates the variable's state.
     *
     * @throws InvariantViolation if an invariant is violated during the
     *         assignment, for example if the value cannot be materialized or
     *         the variable cannot be updated
     */
    @Override
    public void perform() throws InvariantViolation {
        this.variable.assignWith(this.value);
    }
}
