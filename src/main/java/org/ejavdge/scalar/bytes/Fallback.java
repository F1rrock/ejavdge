package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Bytes} decorator that uses an alternative source when the primary
 * source throws {@link InvariantViolation}.
 * <p>
 * A successful result from the primary source is returned unchanged, even
 * when it is empty. The fallback source is evaluated only on failure.
 */
public final class Fallback implements Bytes {

    /**
     * The primary byte source.
     */
    private final Bytes origin;

    /**
     * The fallback source, used if the primary source fails.
     */
    private final Bytes then;

    /**
     * Creates a decorator over primary and fallback byte sources.
     *
     * @param bs the primary source of bytes
     * @param f the fallback source used if the primary source fails
     */
    public Fallback(final Bytes bs, final Bytes f) {
        this.origin = bs;
        this.then = f;
    }

    /**
     * Returns the bytes content, preferring the primary source and falling back
     * to the secondary source on failure.
     * <p>
     * The method first attempts to obtain the content from the primary source
     * via {@link Bytes#content()}. If that call throws an
     * {@link InvariantViolation}, the method instead obtains the content from
     * the fallback source. Any exception thrown by the fallback source is
     * propagated to the caller.
     *
     * @return the bytes from the primary source, or from the fallback source
     *         if the primary source throws {@link InvariantViolation}
     * @throws InvariantViolation if the fallback source fails to produce
     *         content
     */
    @Override
    public byte[] content() throws InvariantViolation {
        try {
            return this.origin.content();
        } catch (final InvariantViolation e) {
            return this.then.content();
        }
    }
}
