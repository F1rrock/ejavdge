package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Num} decorator that requires the underlying numeric value to be
 * strictly positive.
 * <p>
 * This class wraps another {@link Num} instance and enforces an invariant:
 * when {@link #value()} is called, the underlying value is obtained and, if it
 * is less than or equal to zero, an {@link InvariantViolation} is thrown with
 * the message {@code "Value is not positive."}. If the value is greater than
 * zero, it is returned unchanged.
 * <p>
 * This is useful when a value is expected to be strictly positive by contract —
 * for example, a retry count, a page number, or a size — and zero or a negative
 * value would indicate a programming error or invalid input. For a weaker
 * constraint that allows zero, use {@link NonNegative} instead.
 */
public final class Positive implements Num {

    /**
     * The underlying numeric value that must be strictly positive.
     */
    private final Num origin;

    /**
     * Creates a decorator that requires the given numeric value to be strictly
     * positive.
     *
     * @param origin the numeric value that must be greater than zero
     */
    public Positive(final Num origin) {
        this.origin = origin;
    }

    /**
     * Returns the underlying numeric value, requiring it to be strictly
     * positive.
     * <p>
     * The underlying value is obtained via {@link Num#value()}. If it is
     * greater than zero, it is returned unchanged. If it is zero or negative,
     * an {@link InvariantViolation} is thrown with the message
     * {@code "Value is not positive."}.
     *
     * @return the strictly positive numeric value
     * @throws InvariantViolation if the underlying value is not positive or if
     *         an invariant is violated while obtaining it
     */
    @Override
    public int value() throws InvariantViolation {
        final var v = this.origin.value();
        if (v > 0) {
            return v;
        }
        throw new InvariantViolation("Value is not positive.");
    }
}
