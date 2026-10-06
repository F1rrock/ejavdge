package org.ejavdge.workspace.env;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A contract for a mutable environment variable in the workspace.
 * <p>
 * An environment variable has a name and a current value, and can be read and
 * reassigned. This interface abstracts over the concrete mechanism used to
 * store and retrieve the variable's value — for example, an in-memory map, a
 * system property, or a real process environment.
 * <p>
 * Values are represented as {@link Text} when assigned, allowing them to be
 * computed lazily. When read via {@link #value()}, the current value is
 * materialized as a string.
 * <p>
 * Implementations may throw an {@link InvariantViolation} if the variable
 * cannot be read or written, or if the provided value is invalid for the
 * variable.
 */
public interface EnvVariable {

    /**
     * Returns the current value of this environment variable.
     *
     * @return the current value as a string
     * @throws InvariantViolation if the value cannot be retrieved or an
     *         invariant is violated while materializing it
     */
    String value() throws InvariantViolation;

    /**
     * Assigns a new value to this environment variable.
     * <p>
     * The new value is provided as a {@link Text}, which is materialized when
     * the assignment is actually performed. Implementations may validate the
     * value or reject it if it is not acceptable for this variable.
     *
     * @param v the new value to assign
     * @throws InvariantViolation if the assignment cannot be performed, for
     *         example if the value is invalid or the variable is read-only
     */
    void assignWith(final Text v) throws InvariantViolation;
}
