package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.PrintStream;

/**
 * An {@link Out} implementation that writes text to a {@link PrintStream}.
 * <p>
 * This class is the primary output channel for the application, sending text to
 * the console by default. It can be configured with any {@link PrintStream},
 * which allows output to be redirected — for example, to a file, a string
 * buffer, or a test harness — without changing the rest of the application.
 * <p>
 * The content to write is provided as a {@link Text}, which is materialized on
 * demand. This allows the actual output to be computed lazily and ensures that
 * any failure while producing the text is reported as an
 * {@link InvariantViolation}.
 */
public final class Console implements Out {

    /**
     * The underlying print stream to which text is written.
     */
    private final PrintStream src;

    /**
     * Creates a console output that writes to {@link System#out}.
     */
    public Console() {
        this(System.out);
    }

    /**
     * Creates a console output that writes to the given print stream.
     *
     * @param s the print stream to which text will be written
     */
    public Console(final PrintStream s) {
        this.src = s;
    }

    /**
     * Writes the content of the given text to the underlying print stream.
     * <p>
     * The text is materialized via {@link Text#content()} and then printed
     * using {@link PrintStream#print(String)}. No additional formatting or line
     * termination is applied.
     *
     * @param t the text to write
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the text content
     */
    @Override
    public void write(final Text t) throws InvariantViolation {
        this.src.print(t.content());
    }
}
