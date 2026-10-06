package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A structured output sink for text.
 * <p>
 * This is a functional interface representing a destination to which textual
 * output can be written. Implementations may target different media, such as
 * the console, a file, or an in-memory buffer. The content to be written is
 * provided as a {@link Text}, which allows it to be computed lazily and
 * materialized only when the write operation is performed.
 * <p>
 * This interface is used throughout the application as the abstraction for
 * reporting results to the user, allowing the output mechanism to be varied
 * independently of the logic that produces the text. For example, a
 * {@link Console} implementation writes to a {@link java.io.PrintStream}, while
 * other implementations might capture the output for testing or further
 * processing.
 */
@FunctionalInterface
public interface Out {

    /**
     * Writes the content of the given text to this output sink.
     * <p>
     * The text is materialized via {@link Text#content()} and then written to
     * the underlying destination. Implementations should not add any
     * additional formatting or line termination unless specifically documented.
     *
     * @param t the text to write
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the text content or while writing it to the destination
     */
    void write(final Text t) throws InvariantViolation;
}
