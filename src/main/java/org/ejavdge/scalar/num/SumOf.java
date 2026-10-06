package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Num} that represents the sum of two numeric values.
 * <p>
 * This class wraps two {@link Num} instances — a left operand and a right
 * operand — and computes their arithmetic sum when {@link #value()} is called.
 * The operands are materialized on demand, and their integer values are added
 * together using standard integer addition.
 * <p>
 * This is useful for composing numeric values that are themselves computed
 * lazily, such as incrementing an index or combining a base offset with a
 * relative position, without forcing either operand to be evaluated ahead of
 * time.
 */
public final class SumOf implements Num {

    /**
     * The left operand of the sum.
     */
    private final Num left;

    /**
     * The right operand of the sum.
     */
    private final Num right;

    /**
     * Creates a numeric value representing the sum of the given operands.
     *
     * @param left  the left operand
     * @param right the right operand
     */
    public SumOf(final Num left, final Num right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the sum of the two operands.
     * <p>
     * Both operands are materialized via {@link Num#value()}, and their
     * integer values are added together. If either operand fails to produce a
     * value, the corresponding {@link InvariantViolation} is propagated.
     *
     * @return the sum of the left and right operands
     * @throws InvariantViolation if either operand violates an invariant while
     *         computing its value
     */
    @Override
    public int value() throws InvariantViolation {
        return this.left.value() + this.right.value();
    }
}
