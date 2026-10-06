package org.ejavdge.workspace.out;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * An {@link Effect} that writes a text message to an {@link Out} sink.
 * <p>
 * This class adapts a pair of a {@link Text} message and an {@link Out}
 * destination to the {@link Effect} interface. When performed, it materializes
 * the message and writes it to the output via {@link Out#write(Text)}.
 * <p>
 * This is the primary mechanism by which application entry points and other
 * effects produce user-visible output. By representing the write operation as
 * an effect, it can be composed with other effects using
 * {@link org.ejavdge.effect.Sequence}, wrapped with decorators such as
 * {@link org.ejavdge.effect.WithDelay} or
 * {@link org.ejavdge.effect.WithTimeout}, and delegated to from application
 * entry points in a uniform way.
 */
public final class WritingOf implements Effect {

    /**
     * The text message to write.
     */
    private final Text message;

    /**
     * The output sink to which the message is written.
     */
    private final Out out;

    /**
     * Creates a writing effect for the given message and output.
     *
     * @param t the text message to write
     * @param o the output sink to write to
     */
    public WritingOf(final Text t, final Out o) {
        this.message = t;
        this.out = o;
    }

    /**
     * Performs this effect by writing the message to the output.
     * <p>
     * The message is materialized via {@link Text#content()} and passed to
     * {@link Out#write(Text)}, which is responsible for delivering it to the
     * underlying destination.
     *
     * @throws InvariantViolation if an invariant is violated while materializing
     *         the message or while writing it to the output
     */
    @Override
    public void perform() throws InvariantViolation {
        this.out.write(this.message);
    }
}
