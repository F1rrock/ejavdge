package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Bytes} decorator that requires the underlying byte sequence to be
 * non-empty.
 * <p>
 * This class wraps another {@link Bytes} instance and enforces an invariant:
 * when {@link #content()} is called, the underlying content is materialized
 * and, if it turns out to be an empty array, an {@link InvariantViolation} is
 * thrown with the message {@code "byte array is empty."}. If the array
 * contains at least one byte, it is returned unchanged.
 * <p>
 * This is useful when an empty byte array would indicate a violation of the
 * application's assumptions — for example, when a token, cookie, or payload is
 * expected to be present for a subsequent operation to make sense.
 */
public final class NonEmpty implements Bytes {

    /**
     * The underlying byte sequence that must be non-empty.
     */
    private final Bytes origin;

    /**
     * Creates a decorator that requires the given byte sequence to be
     * non-empty.
     *
     * @param x the byte sequence that must contain at least one byte
     */
    public NonEmpty(final Bytes x) {
        this.origin = x;
    }

    /**
     * Returns the content of the underlying byte sequence, requiring it to be
     * non-empty.
     * <p>
     * The underlying content is materialized. If it is an empty array, an
     * {@link InvariantViolation} is thrown with the message
     * {@code "byte array is empty."}. Otherwise, the byte array is returned
     * unchanged.
     *
     * @return the non-empty byte content of the underlying sequence
     * @throws InvariantViolation if the underlying content is empty or if an
     *         invariant is violated while materializing it
     */
    @Override
    public byte[] content() throws InvariantViolation {
        final byte[] x = this.origin.content();
        if (x.length == 0) {
            throw new InvariantViolation("byte array is empty.");
        }
        return x;
    }
}
