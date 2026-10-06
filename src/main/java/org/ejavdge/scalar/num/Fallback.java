package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Num} that provides a fallback value if the primary value cannot be
 * obtained.
 * <p>
 * This class wraps two {@link Num} instances: a primary source and a fallback
 * source. When {@link #value()} is called, it first attempts to obtain the
 * value from the primary source. If that attempt succeeds, the resulting value
 * is returned. If the primary source throws an {@link InvariantViolation}, the
 * exception is caught, and the value is instead obtained from the fallback
 * source.
 * <p>
 * This is useful for optional numeric values where a default or alternative
 * source should be used when the primary source is unavailable. For example, a
 * preset language may be preferred, but if it is not present on the page, the
 * language index from a solution file can be used instead.
 */
public final class Fallback implements Num {

    /**
     * The primary source of the numeric value.
     */
    private final Num origin;

    /**
     * The fallback source, used if the primary source fails.
     */
    private final Num then;

    /**
     * Creates a fallback value from the given primary and fallback sources.
     *
     * @param n the primary source of the numeric value
     * @param f the fallback source used if the primary source fails
     */
    public Fallback(final Num n, final Num f) {
        this.origin = n;
        this.then = f;
    }

    /**
     * Returns the numeric value, preferring the primary source and falling back
     * to the secondary source on failure.
     * <p>
     * The method first attempts to obtain the value from the primary source via
     * {@link Num#value()}. If that call throws an {@link InvariantViolation},
     * the method instead obtains the value from the fallback source. Any
     * exception thrown by the fallback source is propagated to the caller.
     *
     * @return the numeric value from the primary source if available, otherwise
     *         from the fallback source
     * @throws InvariantViolation if the fallback source fails to produce a
     *         value
     */
    @Override
    public int value() throws InvariantViolation {
        try {
            return this.origin.value();
        } catch (final InvariantViolation e) {
            return this.then.value();
        }
    }
}
