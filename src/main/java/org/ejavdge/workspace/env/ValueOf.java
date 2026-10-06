package org.ejavdge.workspace.env;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * An adapter that exposes the current value of an {@link EnvVariable} as a
 * {@link Text}.
 * <p>
 * This class implements {@link Text} and wraps an {@link EnvVariable},
 * delegating the retrieval of textual content to the variable's
 * {@link EnvVariable#value()} method. It allows the value of an environment
 * variable to be used wherever a {@code Text} is expected, such as in string
 * concatenation, formatting, or further processing.
 * <p>
 * Note that the value is not cached: each call to {@link #content()} reads the
 * current value of the underlying variable. If the variable is reassigned
 * between calls, the new value will be observed.
 */
public final class ValueOf implements Text {

    /**
     * The environment variable whose current value is exposed as text.
     */
    private final EnvVariable src;

    /**
     * Creates a new text view over the current value of the given environment
     * variable.
     *
     * @param v the environment variable whose value will be exposed as text
     */
    public ValueOf(final EnvVariable v) {
        this.src = v;
    }

    /**
     * Returns the current value of the underlying environment variable.
     * <p>
     * This method delegates to {@link EnvVariable#value()}, so the returned
     * string reflects the variable's state at the time of the call.
     *
     * @return the current value of the environment variable as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the value
     */
    @Override
    public String content() throws InvariantViolation {
        return this.src.value();
    }
}
