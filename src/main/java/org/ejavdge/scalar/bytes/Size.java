package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;

/**
 * A {@link Num} that represents the size, in bytes, of a {@link Bytes}
 * sequence.
 * <p>
 * This class adapts a {@link Bytes} instance to the {@link Num} interface by
 * exposing the length of its content as an integer. When {@link #value()} is
 * called, the underlying byte sequence is materialized via
 * {@link Bytes#content()}, and the length of the resulting array is returned.
 * <p>
 * This is useful when the size of a byte payload needs to be treated as a
 * numeric value, such as for validation, logging, or formatting purposes.
 */
public final class Size implements Num {

    /**
     * The underlying byte sequence whose size is exposed as a number.
     */
    private final Bytes src;

    /**
     * Creates a numeric view over the size of the given byte sequence.
     *
     * @param bs the byte sequence whose size will be exposed
     */
    public Size(final Bytes bs) {
        this.src = bs;
    }

    /**
     * Returns the size of the underlying byte sequence in bytes.
     * <p>
     * The underlying content is materialized via {@link Bytes#content()}, and
     * the length of the resulting byte array is returned.
     *
     * @return the number of bytes in the underlying content
     * @throws InvariantViolation if an invariant is violated while materializing
     *         the underlying byte sequence
     */
    @Override
    public int value() throws InvariantViolation {
        return this.src.content().length;
    }
}
