package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Num} decorator that requires the underlying numeric value to be
 * non-negative.
 * <p>
 * This class wraps another {@link Num} instance and enforces an invariant:
 * when {@link #value()} is called, the underlying value is obtained and, if it
 * is less than zero, an {@link InvariantViolation} is thrown with the message
 * {@code "Value is negative"}. If the value is zero or positive, it is
 * returned unchanged.
 * <p>
 * This is useful when a value is expected to be non-negative by contract — for
 * example, a count of elements, a retry count, or a slice size — and a
 * negative value would indicate a programming error or invalid input.
 */
public final class NonNegative implements Num {

    /**
     * The underlying numeric value that must be non-negative.
     */
    private final Num origin;

    /**
     * Creates a decorator that requires the given numeric value to be
     * non-negative.
     *
     * @param n the numeric value that must be greater than or equal to zero
     */
    public NonNegative(final Num n) {
        this.origin = n;
    }

    /**
     * Returns the underlying numeric value, requiring it to be non-negative.
     * <p>
     * The underlying value is obtained via {@link Num#value()}. If it is
     * greater than or equal to zero, it is returned unchanged. If it is
     * negative, an {@link InvariantViolation} is thrown with the message
     * {@code "Value is negative"}.
     *
     * @return the non-negative numeric value
     * @throws InvariantViolation if the underlying value is negative or if an
     *         invariant is violated while obtaining it
     */
    @Override
    public int value() throws InvariantViolation {
        final var n = this.origin.value();
        if (n >= 0) {
            return n;
        }
        throw new InvariantViolation("Value is negative");
    }
}
